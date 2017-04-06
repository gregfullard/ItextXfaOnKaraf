# Itext Xfa On Karaf
This is a reference repository to get ItextXFA Worker running on OSGi (Karaf)

Overview
========
The default v5.5.11 release of iText XFA worker is not properly OSGi compatible, which makes it difficult to get it running in an OSGi container (like Karaf). This repository contains sample code and related instructions to get this working.

The repository contains a single OSGi Bundle project which does a VERY simple XFA form render and flattening operation. It uses a hard-coded paths to the XFA Template, XML Data and iText licence files to make the code as simple as humanly possible.

The bundle is coded in such a way that you can invoke the rendering directly in Java SE (by simply invoking a main method inside the JAR) or during the activation method of the bundle in an OSGi container. This makes it simple to confirm that the code works in Java SE and on OSGi

Getting Started
===============
* Clone this repo to your machine. Take note of the folder where you clone it. I'll call that [RepoRoot] in the instructions below
* Paste your own iText licence file into [RepoRoot]/templates/itextkey.xml
* Update the main method in the com.test.itext.Renderer class so that the repoRoot variable contains the path to your [RepoRoot]
* Also update start() method in the com.test.itext.Activator class so that the repoRoot variable contains the path to your [RepoRoot]
* Now you can build the project using "mvn clean install" from the [RepoRoot]/itext-bundle/ folder

Important:
* Take note that in the both the Renderer and Activator invoke a method called renderDoc.
* The first parameter of this method is a boolean called flatten.
* If the parameter is set to false, then one a single interactive XFA will be rendered (This uses the standard Open Source iText libraries)
* If the parameter is set to true, it it will generate the interactive XFA AS WELL AS a flattened version of it (This uses the commercial iText XFA worker library)

Installing Karaf
================
* Download Karaf 4.0.8 from the Apache site (https://karaf.apache.org/download.html)
* Unzip the Kaaf binaries somewhere useful. I'll refer to this folder as [KARAF_HOME]
* To start Karaf:
** Open a Terminal
** cd into [KARAF_HOME]
** run ./bin/karaf
* This will bring you to the Karaf console whick we'll use later.

TODO: If you want to do a clean restart, simply:
* Shutdown Karaf
** Type "shutdown" into the Karaf console
** Confirm by typing Y and Enter
* Delete the [KARAF_HOME]/data folder
* Restart karaf

Testing the solution
====================
Test from your IDE
------------------
* I'm running Eclipse, but I assumed this will work in a similar manner on other IDEs
* Import the Maven project into Eclipse
* Open the com.test.itext.Renderer class
* Right-click, run as "Java Application"
* This will generate two files at [RepoRoot]/templates
** rendered.pdf (and interactive XFA form)
** renderedFlat.pdf (a flattened version of the XFA form)
* If you get this far, the code runs fine

Test from terminal using Jar with Dependencies
----------------------------------------------
* First remember to delete the previously rendered PDFs from [RepoRoot]/templates
* cd into the [RepoRoot]/itext-bundle/ folder
* Build the rendering bundle using "mvn clean install"
* This will build 2 JAR files in the [RepoRoot]/itext-bundle/target folder
** itext-bundle-0.0.1-SNAPSHOT.jar
** itext-bundle-0.0.1-SNAPSHOT-jar-with-dependencies.jar
* Test the rendering from Java SE:
** java -cp target/itext-bundle-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.test.itext.Renderer
* This will generate two files at [RepoRoot]/templates
** rendered.pdf (and interactive XFA form)
** renderedFlat.pdf (a flattened version of the XFA form)

Test from Karaf (no flattening)
-------------------------------
* First remember to delete the previously rendered PDFs from [RepoRoot]/templates
* Then update the flatten parameter to false in the start() method in the com.test.itext.Activator class.
* This will ensure Karaf only uses the Open Source iText libs
* cd into the [RepoRoot]/itext-bundle/ folder
* Build the rendering bundle using "mvn clean install"
* Now start up Karaf
* In the karaf console, install (and start) the bundle straight from your local Maven repo with:
** bundle:install -s mvn:com.test.itext/itext-bundle/0.0.1-SNAPSHOT
* The above command will install and start the bundle
* On bundle startup it will render the rendered.pdf file (it won't render the flattened version, since we set the flag to false)
* 
* 
* 
* 
* 
* This will build 2 JAR files in the [RepoRoot]/itext-bundle/target folder
** itext-bundle-0.0.1-SNAPSHOT.jar
** itext-bundle-0.0.1-SNAPSHOT-jar-with-dependencies.jar
* Test the rendering from Java SE:
** java -cp target/itext-bundle-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.test.itext.Renderer
** You will notice that two
* To install this bundle in Karaf: 
bundle:install -s mvn:com.test.itext/itext-bundle/0.0.1-SNAPSHOT

To test that the class does successfully execute from Java SE:
java -cp target/itext-bundle-0.0.1-SNAPSHOT.jar com.test.itext.Renderer

Same test, but using Jar-with-dependencies:
java -cp target/itext-bundle-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.test.itext.Renderer


If you want to reinstall the bundle into Maven (after manual fixes):
mvn install:install-file -Dfile=itext-bundle-0.0.1-SNAPSHOT.jar   -DgroupId=com.test.itext   -DartifactId=itext-bundle -Dversion=0.0.1-SNAPSHOT -Dpackaging=bundle -DgeneratePom=true

