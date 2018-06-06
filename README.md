# ADV - Algorithm & Data Structure Visualizer

[ ![Download](https://api.bintray.com/packages/adv/adv/adv-ui/images/download.svg) ](https://bintray.com/adv/adv/adv-ui/_latestVersion)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/375ead7886a94d44a480696212c3c53a)](https://app.codacy.com/app/ADV/ADV-UI?utm_source=github.com&utm_medium=referral&utm_content=ADVisualizer/ADV-UI&utm_campaign=badger)
[![Build Status](https://travis-ci.org/ADVisualizer/ADV-UI.svg?branch=develop)](https://travis-ci.org/ADVisualizer/ADV-UI)
[![codecov](https://codecov.io/gh/ADVisualizer/ADV-UI/branch/develop/graph/badge.svg)](https://codecov.io/gh/ADVisualizer/ADV-UI)
<a href="https://structure101.com/"><img src="http://structure101.com/images/s101_170.png" width="90" height="21"></a>

The Algorithm & Data Structure Visualizer (ADV) aids students to easily understand the concepts of several algorithms, taught at the University of Applied Science in Rapperswil (HSR).


## ADV-UI
The ADV-UI is a JavaFX application which is responsible for visualizing the classes of the ADV Lib.
If the UI-JAR can be found on the classpath, the application is automatically started by the ADV-Lib. 
Otherwise the JAR must be started traditionally. (`java -jar adv-ui-<version>.jar`)

### Install
The ADV-UI is available on jCenter and Maven Central. It requires Java 9 or higher.

#### Gradle
````groovy
compile 'ch.hsr.adv:adv-ui:1.0'
````

#### Maven
````xml
<dependency>
  <groupId>ch.hsr.adv</groupId>
  <artifactId>adv-ui</artifactId>
  <version>1.0</version>
</dependency>
````

### Configure socket
If you like to start the socket server on a different port or host, you can start the application with some of the following command line arguments.

```
java -jar adv-ui-<version>.jar --host=192.168.x.x --port=4242
```