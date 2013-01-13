[![Vanilla][Project Logo]][Homepage]

Vanilla is a plugin for the [Spout voxel game platform](https://github.com/SpoutDev/Spout) that provides vanilla [Minecraft](http://minecraft.net) 1.4.7 functionality and gameplay.

[Homepage] | [Forums] | [Twitter] | [Facebook]

## Features
The goal is for Vanilla to feel like vanilla Minecraft, being as close to it as possible, while offering much more freedom.  
You can see a full feature list [here](http://wiki.spout.org/Vanilla#Features), along with what has and hasn't been implemented.

## Using
It's easy to get started! Simply [setup Spout](http://wiki.spout.org/Installing_Spout), and then [place the Vanilla jar in the plugins folder](http://wiki.spout.org/Installing_Vanilla_Plugin)!

## Contributing
Like the project? Feel free to [donate] to help continue development!

Are you a talented programmer looking to contribute some code? We'd love the help!
* Open a pull request with your changes, following our [guidelines and coding standards](http://spout.in/prguide).
* Please follow the above guidelines for your pull request(s) accepted.
* For help setting up the project, keep reading!

## The license
Vanilla is licensed under the [GNU Lesser General Public License Version 3][License], but with a provision that files are released under the [MIT License][License] 180 days after they are published. Please see the `LICENSE.txt` file for details or see [our license in a nutshell](http://spout.in/licensev1).

[![Spout][Company Logo]](http://www.spout.org)

## Getting the source
The latest and greatest source can be found here on [GitHub][Source].

If you are using Git, use this command to clone the project:

    git clone git://github.com/VanillaDev/Vanilla.git

Or download the [latest zip archive][Download Source].

## Compiling the source
Vanilla uses Maven to handle its dependencies.

* Download and install [Maven 3](http://maven.apache.org/download.html)  
* Checkout this repo and run: `mvn clean install`

## Using in your project
If you're using [Maven](http://maven.apache.org/download.html) to manage project dependencies, simply include the following in your `pom.xml`:

    <dependency>
        <groupId>org.spout</groupId>
        <artifactId>vanilla</artifactId>
        <version>1.4.7-SNAPSHOT</version>
    </dependency>

If you do not already have repo.spout.org in your repository list, you will need to add this as well:

    <repository>
        <id>spout-repo</id>
        <url>http://repo.spout.org</url>
    </repository>

If you'd prefer to manually import the latest .jar file, you can get it from our [download site][Download], or from our [build server][Builds]. [![Build Status][Build Icon]][Builds]

Want to know how to use the API? Check out the latest [Javadoc].

[Project Logo]: http://cdn.spout.org/vanilla-github.png
[Company Logo]: http://cdn.spout.org/spout-github.png
[Homepage]: http://www.getvanilla.org
[Forums]: http://forums.spout.org
[License]: http://spout.in/licensev1
[Source]: https://github.com/VanillaDev/Vanilla
[Download]: http://get.spout.org/dev/vanilla.jar
[Download Source]: https://github.com/VanillaDev/Vanilla/archive/master.zip
[Builds]: http://build.spout.org/job/Vanilla
[Build Icon]: http://build.spout.org/job/Vanilla/badge/icon
[Javadoc]: http://jd.spout.org/vanilla/latest
[Issues]: http://issues.spout.org/browse/VANILLA
[Twitter]: http://spout.in/twitter
[Facebook]: http://spout.in/facebook
[Donate]: http://spout.in/donate
