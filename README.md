## Ad Muter

## Description

I like to watch cable news, but I hate watching ads.  This app uses OpenCV to detect the
presence of a logo on a screencap of the browser window where you're watching.

It works in conjunction with this companion Chrome extension I wrote to take screenshots and mute the tab:
https://github.com/jholkeboer/ad-muter-chrome-ext


## Installation

Requres installation of OpenCV in local Maven repo.  See instructions here:
http://docs.opencv.org/2.4/doc/tutorials/introduction/clojure_dev_intro/clojure_dev_intro.html

Create an "/images" folder in the project root.  (TODO remove this manual step)

The CNN logo is provided in the "/resources" folder. (TODO enable swapping logos in the extension)

## Usage

You can run the app as a local web server using `lein run`.

Then install the chrome extension, and click the "A" icon to turn recording on or off.

### Bugs

Currently the logo detection does not scale well when the window is resized.

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
