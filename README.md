# GemPuzzle
GemPuzzle is a small Bukkit plugin that transforms posters (or actually any wall of item frames) into
[gem puzzles][gem-puzzle].
The tiles of a puzzle can be moved by clicking on them.

![Demo gif][demo-gif]

[gem-puzzle]: https://en.wikipedia.org/wiki/15_puzzle
[demo-gif]: https://raw.githubusercontent.com/leMaik/GemPuzzle/master/demo.gif

## Commands and permissions
There are three commands: `/gempuzzle shuffle`, `/gempuzzle create` and `/gempuzzle remove`. They can only be used
ingame. To create and remove puzzles, the player needs the `gempuzzle.manage` permission.

### Shuffle puzzles
To shuffle a puzzle, either power an adjacent block with redstone or run `/gempuzzle shuffle` and then right-click
on the puzzle.

### Create puzzles
To create a new puzzle, remove one item frame of your poster (so that they can be shifted later), run
`/gempuzzle create` and then right-click onto the top-left item frame of the poster. The size will be detected
automatically.

If you don't have a poster yet, check out [PictureFrame][pictureframe], a very simple item frame poster plugin.

### Remove puzzles
To remove a puzzle, run `/gempuzzle remove` and right-click on the puzzle that should be removed.

[pictureframe]: https://github.com/FlorestanII/PictureFrame

## License
GemPuzzle is licensed under the MIT license, see the [license file][license].

[license]: https://github.com/leMaik/GemPuzzle/blob/master/LICENSE