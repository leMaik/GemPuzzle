# GemPuzzle
GemPuzzle is a small Bukkit plugin that transforms posters (or actually any wall of item frames) into [gem puzzles][gem-puzzle].
The tiles of a puzzle can be moved by clicking on them.

![Demo gif][demo-gif]

[gem-puzzle]: https://en.wikipedia.org/wiki/15_puzzle
[demo-gif]: https://raw.githubusercontent.com/leMaik/GemPuzzle/master/demo.gif

## Commands and permissions
There are two commands: `/gempuzzle create` and `/gempuzzle remove`. Both can only be used ingame and require the
`gempuzzle.manage` permission.

### Create puzzles
To create a new puzzle, remove one item frame of your poster (so that they can be shifted later), run `/gempuzzle create` and
then right-click onto the top-left item frame of the poster. The size will be detected automatically.

If you don't have a poster yet, check out [PictureFrame][pictureframe], a very simple item frame poster plugin.

### Remove puzzles
To remove a puzzle, run `/gempuzzle remove` and right-click onto any item frame of the puzzle that should be removed.

[pictureframe]: https://github.com/FlorestanII/PictureFrame

## License
GemPuzzle is licensed under the MIT license, see the [license file][license].

[license]: https://github.com/leMaik/GemPuzzle/blob/master/LICENSE