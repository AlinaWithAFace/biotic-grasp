## Inspiration
Overwatch. Partially the community's [silly projects](https://www.youtube.com/watch?v=_GJ55UIyGvw), and partially one of the newer characters, [Moira](https://playoverwatch.com/en-us/heroes/moira/). It was a sort of perfect blend, with these things on my mind, and then major league hacking had a leap!

## What it does

![Video Demo](ezgif.com-video-to-gif.gif)

Technically, it just types and moves the mouse by interpreting gestures as key presses or mouse movements. However, they're specifically mapped to common first person shooter keys such as WASD, making it possible to technically play games.

## How I built it
Using the [Leap Java SDK](https://developer.leapmotion.com/documentation/java/index.html) and prior Java experience.

## Challenges I ran into
Mouse movements were kind of awkward, coming up with unique gestures that still worked was a little bit of an issue.

## Accomplishments that I'm proud of
It works! I feel like we currently have the technology for a good chunk of crazy sci-fi shenanigans, this is just one area where I actually had some experience and an idea of how to actually do it, which is pretty fantastic.

## What I learned
How the leap API works.

## What's next for Biotic Grasp
Depends. I don't personally have a leap available, so unless I get one at some point it'll probably sit alone forever, or I'll send the link to the streamer who's super into using weird controllers for him to fiddle with it.

If I did eventually get my own leap or another one to work with, there are a couple things I might change/add:

- Looking around via the right hand might take into account the hand's velocity for faster, more responsive turns.
- More gestures for other common keys such as shift (for dashing/sprinting), ctrl (for crouching), and space (for jumping)
- Refactoring because there's a lot of duplicate code
- Gestures for obscure specific-to-Overwatch keys that trigger "hello", "thank you", and other communication hotkeys
- A config file for users to easily edit in their own control schemes
- Refactoring because there's a lot of duplicate code
- Optimizing existing gestures for more consistency
- Add a video demo showing the various gestures available
