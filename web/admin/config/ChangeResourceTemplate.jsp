<%@ page language="java" %>
<%@ page errorPage="/common/Error.jsp" %>
<%@ taglib uri="jstl-fmt" prefix="fmt" %>
<%@ taglib uri="struts-html-el" prefix="html" %>
<%@ taglib uri="struts-tiles" prefix="tiles" %>
<%@ taglib uri="jstl-c" prefix="c" %>
<%--
  NOTE: This copyright does *not* cover user programs that use HQ
  program services by normal system calls through the application
  program interfaces provided as part of the Hyperic Plug-in Development
  Kit or the Hyperic Client Development Kit - this is merely considered
  normal use of the program, and does *not* fall under the heading of
  "derived work".
  
  Copyright (C) [2004-2007], Hyperic, Inc.
  This file is part of HQ.
  
  HQ is free software; you can redistribute it and/or modify
  it under the terms version 2 of the GNU General Public License as
  published by the Free Software Foundation. This program is distributed
  in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
  even the implied warranty of MERCHANTABILITY or FITNESS FOR A
  PARTICULAR PURPOSE. See the GNU General Public License for more
  details.
  
  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
  USA.
 --%>

<script type="text/javascript" src="<html:rewrite page="/js/dojo/dojo.js"/>"></script>
<script language="JavaScript" type="text/javascript">
    dojo.require("dojo.lang.*");
    dojo.require("dojo.widget.Tree"); 
    dojo.require("dojo.widget.TreeContextMenu");

    function onMouseRow(el) {
      el.style.background="#a6c2e7";
    }

    function offMouseRowEven(el) {
      el.style.background="#F2F4F7";
    }

    function offMouseRowOdd(el) {
      el.style.background="#EBEDF2";
    }

var DemoTreeManager = {
  djWdgt: null,
  myTreeWidget: null,
  addTreeContextMenu: function(){
    var ctxMenu = this.djWdgt.createWidget("TreeContextMenu",{});
    ctxMenu.addChild(this.djWdgt.createWidget(
      "TreeMenuItem",{caption:"<c:out value="admin.config.monitoring.EditTemplates"/>", widgetId:"ctxMetrics"}));
    document.body.appendChild(ctxMenu.domNode);
    /* Bind the context menu to the tree */
    ctxMenu.listenTree(this.myTreeWidget);
  },
  addController: function(){
    this.djWdgt.createWidget(
      "TreeBasicController",
      {widgetId:"myTreeController",DNDController:"create"}
    );
  },
  bindEvents: function(){
    /* Bind the functions in the TreeActions object to the context menu entries */
    dojo.event.topic.subscribe("ctxMetrics/engage",
      function (menuItem) { TreeActions.defaultMetrics(menuItem.getTreeNode(),
        "myTreeController"); }
    );
  },
  init: function(){
    /* Initialize this object */
    this.djWdgt = dojo.widget;
    this.myTreeWidget = this.djWdgt.manager.getWidgetById("TypesTree");
    this.addTreeContextMenu();
    this.addController();
    this.bindEvents();
  }
};
 
var TreeActions = {
  defaultMetrics: function(node,controllerId){
    if (node.depth > 0) {
      var appdefType = ("" + node.widgetId).charAt(0);
      if (appdefType == 1) {
        window.location.href = "<html:rewrite page="/resource/platform/monitor/Config.do?mode=configure&aetid="/>" + node.widgetId;
      }
      else if (appdefType == 2) {
        window.location.href = "<html:rewrite page="/resource/server/monitor/Config.do?mode=configure&aetid="/>" + node.widgetId;
      }
      else if (appdefType == 3) {
        window.location.href = "<html:rewrite page="/resource/service/monitor/Config.do?mode=configure&aetid="/>" + node.widgetId;
      }
    }
  }
};
 
    dojo.addOnLoad(function(){
        DemoTreeManager.init()

        dojo.event.topic.subscribe("nodeSelected",
            function(message) {
              var node = message.node;
              if (node.depth > 0) {
                var appdefType = ("" + node.widgetId).charAt(0);
                window.location.href = "<html:rewrite page="/ResourceHub.do?ff="/>" + appdefType + "&ft=" + node.widgetId;
              }
            }
        );
    });
 
</script>

<tiles:insert page="/admin/config/MonitorDefaults.jsp">
  <tiles:put name="message"><fmt:message key="admin.config.message.MonitoringDefaults"/></tiles:put>
</tiles:insert>

