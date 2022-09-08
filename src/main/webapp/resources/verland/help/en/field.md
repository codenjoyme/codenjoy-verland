## Field example

Here's a sample string answer from the server:

<pre>board=☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼**3! *********☼☼**2♥!********☼☼111!*********☼☼ ************☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

The length of the string is equal to the area of the field `N*N`. If you insert a character line break character every `N=sqrt(length(string))` characters, then you get a readable image of the field:

<pre>☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼**3!*********☼
☼**2♥!********☼
☼111!*********☼
☼ ************☼
☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

The first character of the line corresponds to the cell located in the upper left corner and has the coordinate `[0, 14]`.
The coordinate `[0, 0]` corresponds to the lower left corner.
In this example, the position of the hero (symbol `♥`) is `[4, 3]`.

What this field looks like in real life: