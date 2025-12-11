# Breakdown of Files

Jump to day: [1](#day01clj)&nbsp;|&nbsp;[2](#day02clj)&nbsp;|&nbsp;[3](#day03clj)&nbsp;|&nbsp;[4](#day04clj)&nbsp;|&nbsp;[5](#day05clj)&nbsp;|&nbsp;[6](#day06clj)&nbsp;|&nbsp;[7](#day07clj)&nbsp;|&nbsp;[8](#day08clj)&nbsp;|&nbsp;[9](#day09clj)&nbsp;|&nbsp;[10](#day10clj)&nbsp;|&nbsp;[11](#day11clj)&nbsp;|&nbsp;[12](#day12clj)

Here is a breakdown of the various files in this directory. Files with names of
the form `dayNN.clj` represent the code actually used to solve the problems
(with some tweaking done using a static analysis plug-in for Leiningen). Files
with `bis` in the name are modified/tuned versions of the given original day.
(If you see comments in a file, I can usually promise you they were added after
the fact.)

The numbers in parentheses in the descriptions of the files represent the rank
I had for when my solutions were submitted and accepted. Time, if given, is a
rough estimate of how long it took to solve both halves.

A given day and part can be run via:

```
lein run DAY PART
```

where `DAY` is a number from 1-25 and `PART` is 1 or 2. If there is a "bis"
version of a day, that can be run via:

```
lein run -b DAY PART
```

## [day01.clj](day01.clj)

Day 1 (58:46).

Off to a great start. Got both halves wrong on the first tries.

## [day02.clj](day02.clj)

Day 2 (1:56:35, actual time ~28:00 due to late start).

This is more like it. I owe the fast solves more to Perl than to Clojure, as
regular expressions be the rule of the day.

Part 1 was to identify any of the ID values that were made entirely of a
sequence of one or more digits followed by the same sequence. For this, the
pattern `^(\d+)(\1)$` applied to a stringified version of the ID worked.

For part 2 it was extended to be any number of repeats of the sequence (two or
more). For that, the pattern was `^(\d+)(?:\1)+$`.

## [day03.clj](day03.clj)

Day 3 (45:47 for part 1, unknown for part 2).

This escalated quickly. Part 1 was solvable with a semi-brute force approach,
in which for each battery bank I picked the first highest number that wasn't in
the last slot, then from its position forward picked the highest number for the
second battery.

Part 2 upped this to 12 batteries from each bank. I couldn't think of a good
approach other than to reach for the combinatorics module, and I was pretty
sure that would be too large of a search-space. I looked at some other code on
reddit, and lit up when I saw someone had used dynamic programming (in Python
in this case). I adapted it to Clojure with a slightly more-functional feel. At
first I wasn't getting the right answers, but it turned out that the value I
was using for "negative infinity" was too high.

I need to get better at dynamic programming, both in writing it and in knowning
when to reach for it in the toolbox.

## [day04.clj](day04.clj)

Day 4 (1:35:40, but started 1:05:00 late).

Solved part 1 in 16:39 and part 2 14 minutes later (with time taken for getting
a drink and a snack).

This is a style of puzzle that Clojure is really well-suited for: traversing a
field/matrix and looking around. Part 1 was just to count how many "paper roll"
squares could be accessible based on the puzzle rules. It took too long to get
part 1 to work on the test data because I was going too fast and made silly
mistakes.

Part 2 was to actually *remove* the reachable rolls, iterate this process until
no more were accessible, and return the count of rolls removed. It only took 14
minutes because I got up to get more to drink, clear dishes, and serve a snack
to myself and my wife. Because Clojure doesn't change data when you update it,
it is very easy to create a "new" field with the accessible elements removed
while maintaining the original field. Couple this with the ability to directly
compare two such structures for equality, and the loop is pretty short and
readable.

## [day05.clj](day05.clj)

Day 5 (12:01:12, but started ~2:00:00 late).

Solved part 1 in about 19:33, slowed down only by the fact that the numbers
(and hence the ranges) were much too large for my usual approach of adding
everything to a `set` structure and testing against it. Part 1 is essentially
O(_mn_), as I am looping over all ranges for each ingredient in the list. There
is likely a better way for that.

Part 2 gave me a wrong answer the first try (error no. 3 for this year). I
realized my algorithm for merging the overlapping ranges was wrong, but by this
time it was nearly midnight in my time-zone so I decided to sleep on it. This
morning, I did some searching for an algorithm, and also looked at the
[part 2 solution from Norman Richards](https://gitlab.com/maximoburrito/advent2025/-/blob/main/src/day05/main.clj).
While I liked the clarity of the code, it was looping over the set of ranges in
a nested fashion, making it O(_n<sup>2</sup>_). I instead took an algorithm
from
[AlgoCademy](https://algocademy.com/blog/merge-intervals-a-comprehensive-guide-to-solving-this-classic-algorithm-problem/)
that does a single sorting of the intervals followed by a single-pass over the
list. This got me the correct answer, in really short order no less.

## [day06.clj](day06.clj)

Day 6 (2:19:52, started ~1:20:00 late).

Solved part 1 in about 32 minutes, much of which was debugging non-bugs caused
by fatigue and headache from attending a hockey game. The approach was to
create a matrix of the numbers and operands (operands being the last row). This
yielded rows in which the operand (now converted to the corresponding function)
was the first element and the numbers the rest of the row. Operands were
applied to the numbers, and the results summed.

Part 2 was trickier, of course. It took about 28 minutes in all, thanks to not
making any more non-bug errors. Here, the entire text was converted into a
matrix. That was then rotated once counter-clockwise (actually 3 times
clockwise). Each row was converted back to a string, and each string was one of
three things: A stand-alone number, a number followed by an operand, or a
sequence of only spaces. This was fed into a `loop` that examined each
non-space string for a number and a possible operand. Numbers were gathered,
and when an operand was found it was applied to the gathered numbers, the
result added to a second accumulator, and the loop would iterate with an empty
group of numbers and the newly-extended accumulator. At the end, the contents
of the accumulator were summed.

## [day07.clj](day07.clj)

Day 7 (1:04:36, started ~15:00 late).

Part 1 took 25:22 or so, much of which was spent trying to understand how to
get 21 as an answer out of the test data. I coded what I *thought* would be
the process, and luckily I was right. But it was at least 75% guessing at that
point.

Part 2 was to count the number of "timelines" that the particle creates as it
goes through all the splitters. This was actually easier to envision, but a
silly mistake took longer to debug than it should have. It took an additional
24 minutes or so to finish part 2.

## [day08.clj](day08.clj)

Day 8 (1:14:31, started ~15:00 late).

Part 1 took nearly 50 minutes due to trying to find a bug but looking in all
the wrong places. Once the one bug was found, it ran fine.

Part 2 *looked* like it would be scary, but in the end it ran about 0.2s
FASTER than part 1 had.

## [day09.clj](day09.clj)

Day 9 (19:33:18, started about 1:22:00 late, slept on part 2).

Where part 1 was trivial, part 2 stymied a LOT of players. For part 1, it was
a matter of getting all combinations of the red tiles and computing area for
each proscribed rectangle. Taking the largest of the areas gave the answer.

Part 2 involved finding the area of the largest rectangle for which all points
were within the polygon described by the sequence of points. While there are
algorithms for this, all but my last attempt failed to get the right answer. I
don't know if the fact that every edge-slope was either 0 or undefined
(division by zero). I finally found a succint (if slow) approach in Python that
I was able to convert to Clojure. But I'm dying to know why more "traditional"
approaches consistently failed.

## [day10.clj](day10.clj)

Day 10 (17:10:34).

Another day where I had to sleep on part 2, and it didn't really help at all
this time.

Part 1 was straightforward. Most people seemed to have chose BFS, I just
iterated over increasing numbers of button-press combinations. It probably
would have gone faster with BFS.

Part 2, well, I wouldn't have gotten part 2 at all unless I had seen another
Clojurist using the `fastmath` library to solve it as a series of linear
equations. I read the docs, studied his code
([here](https://github.com/genmeblog/advent-of-code/blob/master/src/advent_of_code_2025/day10.clj)),
and still made mistakes. In the end, my code looks mostly like his, just
because I was getting frustrated and didn't want this hanging over my head
when day 11 unlocks tonight. I do plan to examine/investigate the `fastmath`
package in greater depth.

## [day11.clj](day11.clj)

Day 11 (12:44:51).

Finished part 1 in just over 15 minutes (accounting for having started 5
minutes late). Part 2 *looked* like it would be a simple extension of part 1,
based on how I'd solved part 1. But (of course) it wasn't. I ended up leaving
part 2 running while I went to sleep, but even 8 hours later it still hadn't
completed.

I initially solved part 1 with a simple BFS, using the `PriorityQueue` that
comes with Clojure (a favorite of mine for these sorts of problems). I got it
right the first time, with a run-time of about 40ms.

Part 2 would never have worked with that-- I even had trouble running the test
data for part 2 with the modified code. So, I went looking for a better
approach. I found an algorithm that used memoization and recursion, and adapted
it to Clojure. It appeared fine, and got the test-data answer correct. In fact,
I adapted part 1 to use this code and dropped the run-time to 8ms! But it ran
forever trying to do part 2. I decided to sleep on it, and let it run while
sleeping. A little over 8 hours later, it still hadn't solved part 2.

Getting help from [narimiran](https://narimiran.github.io/aoc2025/) on the
Clojure slack instance, I learned that I *hadn't* actually really memoized the
core recursive function. It was running the problem without any memoization
hits. I rewrote the memoization part in a different structure (getting a
warning from `clj-kondo` in the process that I can't clear). But it worked. I
got the answer for part 2 in 17ms, and re-ran part 1 as well. It went down to
5ms, which is essentially and order of magnitude reduction in run-time.

## [day12.clj](day12.clj)

Day 12 ().
