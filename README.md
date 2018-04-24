## What it does

![Video Demo](ezgif.com-video-to-gif.gif)

Technically, this thing types and moves the mouse by interpreting gestures as key presses or mouse movements. However, they're mapped to common first person shooter keys such as WASD, making it possible to play games.

To be more specific, it partitions the leap's viewable area in half for each hand to have it's own space. The left hand's position is used to control movement when it's outside the central dead zone. In other words it pushes W to go forward, D to go right, S to go backwards, and A to go left.

The right hand is used to look around, or control mouse movements. Rather than being positional, it's based on which direction your palm appears to be pointing and moves accordingly if you're outside a small forward facing dead zone. Currently it only works for looking left and right, but ideally it would work for looking all directions, #PipeDreams.

Beyond that, there are also different gestures in place that press different keys to trigger Moira's abilities. For example, pointing all the fingers on your right hand up triggers a right click, or her main damage ability/secondary fire. Pointing all the fingers on your left hand triggers a left click, or her main healing ability/primary fire. Point all 10 of your fingers forward triggers an "e", which ingame brings up a little UI to trigger a healing orb or damage orb, which can then be selected by the same right click or left click gesture. There's also a gesture that triggers her ultimate if you have your four left fingers pointing directly up and your thumb pointing directly right.

None of the gestures are "real" in the sense that they're all positionally based rather than relative to the hand itself, which has room for improvement. It might feel more intuitive if, for example, they were based on fingers being relative to each other rather than or in addition to the overall direction.

## How I built it
Using the [Leap Java SDK](https://developer.leapmotion.com/documentation/java/index.html) and prior Java experience. There was an attempt to build it in C#, but I haven't worked with it enough and time constraints during the hackathon made me reevaluate that decision to eventually make this in Java.

## Challenges I ran into
Mouse movements were kind of awkward, coming up with unique gestures that still worked was a little bit of an issue.

## Accomplishments that I'm proud of
It works! I feel like we currently have the technology for a good chunk of crazy sci-fi shenanigans, this is one area where I actually had some experience and an idea of how to actually do it, which is pretty fantastic.

## What I learned
How the leap API works, and a bit about how to use flags for input.

## What's next for Biotic Grasp
Depends. I don't personally have a leap available, so unless I get one at some point it'll sit forever.

If I did get my own leap or another one to work with, there are a couple things I might change/add:

- ~~Looking around via the right hand might take into account the hand's velocity for faster, more responsive turns.~~
- ~~More gestures for other common keys such as shift (for dashing/sprinting), ctrl (for crouching), and space (for jumping). Shift might be pointing all of your fingers backwards, as if you're swimming through the air. Crouching might work by positioning your hands below a vertical dead zone, and jumping might be positioning your hands above it.~~
- ~~Refactoring because there's a lot of duplicate code~~
- ~~Gestures for specific-to-Overwatch keys that trigger "hello", "thank you", "group up", and other communication hotkeys.~~
- A config file for users to easily edit in their own control schemes
- ~~Refactoring because there's a lot of duplicate code~~
- Optimizing existing gestures for more consistency
- Add a video or visual demo showing the various gestures available
- Add a ui of sorts to show when the controls are working properly vs when they're almost-but-not-quite there. I'm thinking a sort of overlay that shows the skeleton of your hand and maybe highlights when a given finger is pointed up/forward correctly, just to visually confirm you're doing it right. It might also have a visual depiction of the bounds for movement/looking around so you have a visual frame of reference.
- Some people have asked about different control schemes for different characters, which could happen but it's a bit awkward to make work intuitively because everyone else in Overwatch holds a weeapon. Moira is unique in that she attacks and make an impact without holding a physical object because ingame plot shenanigans. But yeah, it would be cool to have other gestures match the character you're playing.


## 2018-04-23

So, I'm pretty happy with this for the moment in terms of how it works, but there's a distinct lack of visuals which isn't helpful. I guess the next move is to make something to that effect to help you figure out what you're doing. My first thought was to sort of port it to Unity and maybe highlight or change the colors of your hands if a particular gesture (really they're more like positions...) has been activated. Then maybe from there add some gif tutorials of sorts, but we'll see.
