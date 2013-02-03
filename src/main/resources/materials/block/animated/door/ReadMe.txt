For each kind of door (wooden and iron), there are 2 parts : Top part and Bot part of the door.
For each part of each door, there are 2 states : open or close.
Finally, it's 8 obj files named in this form : [kind]_[part]_[state].obj

door_animated.ske
is the skeletton of the door, to make the opening or closing animation.

door_opening.sam
and
door_closing.sam
are the two animations you can use.

/!\ IMPORTANT /!\
The .ske and the .sam must be applied only on close doors' obj ! If it's done on open doors, it will make some strange stuff... ;)