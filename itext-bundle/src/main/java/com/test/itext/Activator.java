/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.test.itext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) {
        System.out.println("Starting the bundle");
        System.out.println("Rendering PDF");
        Renderer rend = new Renderer();
        
        String repoRoot = "/home/gregf/Greg_OfflineData/Dev/Repos/Other/ItextXfaOnKaraf/";
        
        String templatePath = repoRoot + "templates/BasicXfa.pdf";
        String xfaPdfPath = repoRoot + "templates/rendered.pdf";
    	String flatPdfPath = repoRoot + "templates/renderedFlat.pdf";
    	String licenseFilePath = repoRoot + "templates/itextkey.xml";

        String renderPayload = "<form1><Name>MyName</Name><Surname>MySurname</Surname></form1>";
        
    	boolean flatten = false;
        rend.renderDoc(flatten, templatePath, renderPayload, xfaPdfPath, flatPdfPath, licenseFilePath);    	
    }

    public void stop(BundleContext context) {
        System.out.println("Stopping the bundle");
    }

}