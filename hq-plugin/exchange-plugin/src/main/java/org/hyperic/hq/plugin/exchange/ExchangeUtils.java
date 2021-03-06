package org.hyperic.hq.plugin.exchange;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.win32.RegistryKey;
import org.hyperic.sigar.win32.Win32Exception;
import org.hyperic.util.exec.Execute;
import org.hyperic.util.exec.PumpStreamHandler;
import org.hyperic.hq.product.ServiceResource;
import org.hyperic.util.config.ConfigResponse;
import org.hyperic.hq.product.ServerDetector;

import org.hyperic.sigar.win32.Pdh;
import org.hyperic.util.exec.ExecuteWatchdog;

public class ExchangeUtils {
    
    protected static final String EXCHANGE_ROLE_REG_KEY = "EXCHANGE_ROLE_REG_KEY";
    protected static final String DAG_DISCOVERY = "DAG_DISCOVERY";
    public static final String DAG_NAME = "DAG_name";
    protected static final String SITE_DISCOVERY = "SITE_DISCOVERY";
    public static final String AD_SITE_PROP = "active_directory.site";
    

    protected static final String TRANSPORT =
        ExchangeDetector.EX + "Transport";

    protected static final String SMTP_SEND =
        "SmtpSend";

    protected static final String SMTP_RECEIVE =
        "SmtpReceive";

    
    private static final Log log =
            LogFactory.getLog(ExchangeUtils.class.getName());
    public static final String POWERSHELL_COMMAND = "C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe";

    
    protected static boolean checkRoleConfiguredAndSetVersion(String roleRegKeyStr, ConfigResponse cprops) {        
        // check if role exists in registry
        RegistryKey  key = null;
        try {
            key = RegistryKey.LocalMachine.openSubKey(roleRegKeyStr);
            String version = key.getStringValue("ConfiguredVersion").trim();
            if (log.isDebugEnabled()) {
                log.debug("version=[" + version +"]");
            }            
            if (version != null) {
                cprops.setValue("version", version);
            }
            return true;
        }
        catch (Win32Exception e) {            
            log.debug("ExchangeUtils: didn't find ConfiguredVersion in:" + roleRegKeyStr);
            return false;
       }           
       finally {
           if (key != null) {
               key.close();
           }
       }
        
    }
    
    public static String fetchActiveDirectorySiteName() {
        String resultSiteName="";
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Execute exec = new Execute(new PumpStreamHandler(output));
            exec.setCommandline(
                    new String[]{"nltest", "/DsGetSite"});
            int rc = exec.execute();
            String out = output.toString().trim();
            if (log.isDebugEnabled()) {
                log.debug("output of "+exec.getCommandLineString()+" : " + out);
            }
            if (rc == 0) { 
                BufferedReader in = null;
                in = new BufferedReader(new StringReader(output.toString()));
                resultSiteName=in.readLine();
            }
        } catch (Exception e) {
            log.debug("Failed to fetch active directory site name: " + e.getMessage(), e);
        }
        return resultSiteName;
    }
    
    protected static List discoverPerfTransportServices(String type, ServerDetector detector) {
        List services = new ArrayList();

        try {
            String[] instances =
                Pdh.getInstances(ExchangeUtils.TRANSPORT + " " + type);

            for (int i=0; i<instances.length; i++) {
                String name = instances[i];
                if (name.equalsIgnoreCase("_Total")) {
                    continue;
                }
                
                ServiceResource service = new ServiceResource();
                service.setType(detector, type);
                service.setName(type + " " + name);

                ConfigResponse config = new ConfigResponse();
                config.setValue("name", name);
                service.setProductConfig(config);
                service.setMeasurementConfig();

                services.add(service);
            }
        } catch (Win32Exception e) {
            log.debug(e, e);
        }

        return services;
        
    }

    public static String runCommand(String[] command, int timeout) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        final PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(output);
        ExecuteWatchdog wdog = new ExecuteWatchdog(timeout * 1000);
        Execute exec = new Execute(pumpStreamHandler, wdog);
        exec.setCommandline(command);
        log.debug("Running: " + exec.getCommandLineString());
        try {
            exec.execute();
        } catch (Exception e) {
            log.debug("Fail to run command: " + exec.getCommandLineString() + " " + e.getMessage());
            return null;
        }
        String out = output.toString().trim();
        return out;
    }
}
