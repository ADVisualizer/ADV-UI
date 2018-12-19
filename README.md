# ADV - Algorithm & Data Structure Visualizer

[ ![Download](https://api.bintray.com/packages/adv/adv/adv-ui/images/download.svg) ](https://bintray.com/adv/adv/adv-ui/_latestVersion)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/375ead7886a94d44a480696212c3c53a)](https://app.codacy.com/app/ADV/ADV-UI?utm_source=github.com&utm_medium=referral&utm_content=ADVisualizer/ADV-UI&utm_campaign=badger)
[![Build Status](https://travis-ci.org/ADVisualizer/ADV-UI.svg?branch=develop)](https://travis-ci.org/ADVisualizer/ADV-UI)
[![codecov](https://codecov.io/gh/ADVisualizer/ADV-UI/branch/develop/graph/badge.svg)](https://codecov.io/gh/ADVisualizer/ADV-UI)
<a href="https://structure101.com/"><img src="http://structure101.com/images/s101_170.png" width="90" height="21"></a>

The Algorithm & Data Structure Visualizer (ADV) helps students to understand the concepts of several data structures and algorithms, taught at the University of Applied Science in Rapperswil (HSR).


## ADV-UI
The ADV-UI is a JavaFX application responsible for visualizing the classes of the [ADV Lib](https://github.com/ADVisualizer/ADV-Lib).
If the UI-JAR can be found on the classpath, the application is automatically started by the ADV-Lib. 
Otherwise the JAR must be started manually.

### Install
The ADV-UI is available on jCenter. It requires Java 11 or higher.

#### Gradle
````groovy
compile 'ch.hsr.adv:adv-ui:2.0'
````

#### Maven
````xml
<dependency>
  <groupId>ch.hsr.adv</groupId>
  <artifactId>adv-ui</artifactId>
  <version>2.0</version>
</dependency>
````

### Start
````bash
java -jar /path/to/adv-ui-<version>.jar
````

#### Configure socket
If you want to start the socket server on a different port or host, you can use the following command line arguments.

```
java -jar adv-ui-<version>.jar --host=192.168.x.x --port=4242
```