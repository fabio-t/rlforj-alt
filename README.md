# rlforj-alt

[![Latest Github release](https://img.shields.io/github/release/fabioticconi/rlforj-alt.svg)](https://github.com/fabioticconi/rlforj-alt/releases/latest)
[![Build Status](https://travis-ci.org/fabioticconi/rlforj-alt.svg?branch=master)](https://travis-ci.org/fabioticconi/rlforj-alt)

Roguelike Library For Java (Alternative version).

The main aim of this library is to provide a set of simple, yet efficient, algorithms for common problems in
the development of roguelikes and other games:

* **FoV**: the field of view is the area around a game entity that is visible. Commonly, things like walls and trees
  block vision. This is provided in both circular and conic format.
  
* **LoS**: the line of sight is a linear path from a starting position to a target position, provided there
  no obstacles between them. It's useful for ranged attacks, for example.
  
* **Pathfinding**: given a map, we want to find the optimal, nonlinear path between a start and end points. This
  may also take account of the terrain, to give preference to paths avoiding slow-moving terrains, for example.
  
Some terrain generation utilities will also be available in the future.

## Install

1. First add Jitpack as a repository inside your pom.xml

```$xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

2. Then add `rlforj-alt` as a dependency (make sure to change `version` to one of the
[releases](https://github.com/fabioticconi/rlforj-alt/releases)):

```$xml
<dependency>
    <groupId>com.github.fabioticconi</groupId>
    <artifactId>rlforj-alt</artifactId>
    <version>version</version>
</dependency>
```

## Use

The main component of rlforj-alt is the so called `Board`. You must implement the [IBoard](src/main/java/rlforj/IBoard.java)
interface before doing anything:

```$java
public interface IBoard
{
    boolean contains(int x, int y);

    boolean blocksLight(int x, int y);

    boolean blocksStep(int x, int y);

    void visit(int x, int y);
}
```

* `contains` should check boundaries of your map/level

* `blocksLight` should return true for all positions that block vision (eg, walls). Used by Fov/Los algorithms

* `blocksStep` should return true for all positions that block movement. Used by pathfinding algorithms

* `visit` will be called by the Fov/Los algorithms on each visible cell. You may
  use it to store the visible cells or to manipulate the map, or whatever you wish. See below for details on how it's
  used.

### Field of View

To explore all the visible cells (what is generally called `Field of View`) you must choose one of the available
Fov algorithms implementing the [IFovAlgorithm](src/main/java/rlforj/los/IFovAlgorithm.java) interface:

```$java
public interface IFovAlgorithm
{
    void visitFov(IBoard b, int x, int y, int distance);
}
```

Then you can use it like this:

```$java
IBoard map = new MyMap();

// choose one of these
IFovAlgorithm a = new ShadowCasting();
IFovAlgorithm a = new PrecisePermissive();

// calls IBoard#visit on all visible cells from the origin and up to the given radius
a.visitFov(map, originX, originY, radius);
```

The method `IFovAlgorithm#visitFov` is guaranteed to call `IBoard#visit` **only once**, and always before
`IBoard#blocksLight`. This last thing is important for things like a bomb or fireball: whatever blocking cell the
Fov algorithm encounters, it's destroyed in the `FireballBoard#visit` - and thus the Fov will continue to visit surrounding
cells that were previously shadowed by this cell.

`IBoard#blocksLight` and `IBoard#blocksStep` can be called multiple times at each location.

### Conic Field of View

In alternative to the above, if you want to only visit a **conic field of view** (eg, for breathing fire, or to simulate a
directional light), you can choose one of the algorithms implementing the [IConeFovAlgorithm](src/main/java/rlforj/los/IConeFovAlgorithm.java)
interface:

```$java
public interface IConeFovAlgorithm
{
    void visitConeFov(IBoard b, int x, int y, int distance, int startAngle, int endAngle);
}
```

Then you can use it like this:

```$java
IBoard map = new MyMap();

// choose one of these
IConeFovAlgorithm a = new ShadowCasting();
IConeFovAlgorithm a = new ConePrecisePermissive();

// visit all visible cells from the origin and up the given radius, in a cone of 180 degrees
a.visitConeFov(map, originX, originY, radius, 0, 180);
```

### Line of Sight

To find a straight, unobstructed line between a start and end point is a task called `Line of Sight`. This is very
useful for ranged attacks.

rlforj-alt supports many Los algorithms, all implementing the [ILosAlgorithm](src/main/java/rlforj/los/ILosAlgorithm.java)
interface:

```$java
public interface ILosAlgorithm
{
    boolean exists(IBoard b, int startX, int startY, int endX, int endY, boolean savePath);

    List<Point> getPath();
}

```

If you only need to **test** for line of sight, set the last argument, `savePath`, to `false`.

More commonly, you will need the path (if one exists), so do this:

```$java
IBoard map = new MyMap();

// choose one of these
ILosAlgorithm a = new BresLos(symmetric); // symmetric can be true or false
ILosAlgorithm a = new BresOpportunisticLos();
ILosAlgorithm a = new ShadowCasting();
ILosAlgorithm a = new PrecisePermissive();

List<Point> path;
if (a.exists(map, startX, startY, endX, endY, true))
  path = a.getPath();
else
  // do something else?
```

Example of line of sight path:

```
...##...........#
.................
#...........#..*#
###...#......./..
............./...
..........#/-....
#........./......
........#/..#....
....#...@..#.....
........#........
.......#...#.....
...#......#......
...#..#......#...
#.........#......
...#.#...........
...#.............
.............#...
```

If you are unsure which Los algorithm to use, consider this:

- If you need absolute certainty that **if a point is in your Field of View, then is also in your Line of Sight**,
  then use the same algorithm, `ShadowCasting` or `PrecisePermissive`, for Los too
  
- If you need **faster and prettier** Line of Sight, then choose one of the `Bresenham`. We advise you to use
  `BresLos(true)`, eg symmetric Bresenham, unless you have a reason not to
  
- all algorithms return the same or very similar paths in "good" cases, but may vary wildly in more complicated ones

A final note: if a line of sight cannot be established, the path will be `null`. If `path` is not `null`, it always
includes the start and end point.

### Pathfinding

Pathfinding is the task of finding an unobstructed path from a start to an end point. Contrarily to Los,
the path does not need to be a line.

Only one algorithm is supported for now, the king of pathfinding: AStar. Use it like this:

```$java
IBoard map = new MyMap();

// choose one of these
diag = true; // true if you allow diagonal movement, false otherwise
AStar a = new AStar(map, map.width(), map.height(), diag);

radius = -1; // this will search the whole board - it can be very expensive to do so!
radius = 10; // this will only search the cells in a radius of 10 cells from the starting point
Point[] path = a.findPath(startX, startY, endX, endY, radius)
```

If `path` is not `null`, it always includes the start and end point.

## Examples

Let's see some of the algorithms in action. If you wish to run them yourself, have a look at the
[examples folder](src/main/java/rlforj/examples/).

### Fov ShadowCasting

```                     
     .#...       
    ......  .    
   . ..... ...   
  ...#...##....  
    ...........  
     #.........  
      #.@......  
   ............  
  .............  
  ..#.#....#.#.  
   ...##...# .   
    #  .....     
       .....      
```

### Fov PrecisePermissive

```  
     .#...       
    ......  .    
   . ..... ...   
  ...#...##....  
   ............  
     #.........  
      #.@......  
  .............  
  .............  
  ..#.#....#.#.  
    ..##...# .   
    #  .....     
       .....     
```
    
### Conic Fov ShadowCasting
  
```                     
          @          
          ....       
           ....      
           ......    
           ......#   
            .......  
            .....#.. 
            ........ 
             ....... 
             #....#. 
```
                     
### Conic Fov PrecisePermissive
        
```                     
          @          
          ...        
           .....     
           ........  
           ......#   
            .......  
            .....#   
            .......  
            ......   
            ##...    
              #      
```

Disclaimer
---------

This was originally a fork from [kba/rlforj](https://github.com/kba/rlforj) (version 3.0), but has since diverged.
See the LICENSE file for copyright information.
