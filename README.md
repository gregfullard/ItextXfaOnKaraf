# Itext Xfa On Karaf
This is a reference repository to get ItextXFA Worker running on OSGi (Karaf)

The default v5.5.11 release of iText XFA worker is not properly OSGi compatible, which makes it difficult to get it running in an OSGi container (like Karaf). This repository contains sample code and related instructions to get this working.

The repository contains a single OSGi Bundle project which does a VERY simple XFA form render and flattening operation. It uses a hard-coded paths to the XFA Template, XML Data and iText licence files to make the code as simple as humanly possible.

Getting Started
===============
