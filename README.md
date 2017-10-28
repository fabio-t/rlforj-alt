rlforj-alt
=========

[![Build Status](https://travis-ci.org/fabioticconi/rlforj-alt.svg?branch=master)](https://travis-ci.org/fabioticconi/rlforj-alt)

Roguelike Library For Java (Alternative version).

The main aim of this library is to provide a set of simple, yet efficient, algorithms for common problems in
the development of roguelikes and other games:

* **FoV**: the field of view is the area around a game entity that is visible. Commonly, things like walls and trees
  block vision. This is provided in both circular and conic format.
  
* **LoS**: the line of sight is a linear path from a starting position to a target position, provided there
  no obstacles between them. It's useful for ranged attacks, for example.
  
* **Pathfinding**: given a map, we want to find the optimal, nonlinear path between a start and end points. This
  may also take account of the terrain, to give preference to paths avoiding slow-moving terrains for example.
  
Some terrain generation utilities will also be available in the future.

Installation
------------

1. First add Jitpack as a repository inside your pom.xml

```$xslt
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

2. Then add `rlforj-alt` as a dependency (make sure to change `version` to one of the
[releases](https://github.com/fabioticconi/rlforj-alt/releases)):

```$xslt
<dependency>
    <groupId>com.github.fabioticconi</groupId>
    <artifactId>rlforj-alt</artifactId>
    <version>version</version>
</dependency>
```

Disclaimer
---------

This was originally a fork from [kba/rlforj](https://github.com/kba/rlforj) (version 3.0), but has since diverged.
See the LICENSE file for copyright information.
