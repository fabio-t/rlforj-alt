rlforj-alt
=========

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

Disclaimer
---------

This was originally a fork from [kba/rlforj](https://github.com/kba/rlforj) (version 3.0), but has since diverged.
See the LICENSE file for copyright information.
