[![Vanilla][Project Logo]][Website]

What is Vanilla?
----------------
Vanilla is a plugin for the Spout platform that provides vanilla Minecraft functionality and gameplay.

Copyright (c) 2011-2012, VanillaDev <<http://www.spout.org/>>

Who is VanillaDev?
------------------------
VanillaDev is the team behind the Vanilla and BukkitBridge projects.  
[![Zidane](https://secure.gravatar.com/avatar/99532c7f117c8dac751422376116fb38?d=mm&r=pg&s=48)](http://forums.spout.org/members/zidane.7/) [![ZNickq](https://secure.gravatar.com/avatar/2d9c36328b81d872ba0c5b9cb82bbfe8?d=mm&r=pg&s=48)](http://forums.spout.org/members/znickq.72/) [![Windwaker](https://secure.gravatar.com/avatar/942913bba29c93344d8a2e4da56c6bf1?d=mm&r=pg&s=48)](http://forums.spout.org/members/windwaker.47/) [![bergerkiller](https://secure.gravatar.com/avatar/231ba19298225157537674cbeb7a9f7f?s=mm&r=pg&s=48)](http://forums.spout.org/members/bergerkiller.3753/) [![DDoSQc](https://secure.gravatar.com/avatar/ec0cc434d9c9b34670d4c8845fe6bebc?s=mm&r=pg&s=48)](http://forums.spout.org/members/ddos.5524/) [![Pamelloes](https://secure.gravatar.com/avatar/a48c6394ae3408c5da63b4a5fc2ad3c6?s=mm&r=pg&s=48)](http://forums.spout.org/members/pamelloes.38/) 

Visit our [website][Website] or get support on our [forums][Forums].  
Track and submit issues and bugs on our [issue tracker][Issues].

[![Follow us on Twitter][Twitter Logo]][Twitter][![Like us on Facebook][Facebook Logo]][Facebook][![Donate to the Vanilla project][Donate Logo]][Donate]

Source
------
The latest and greatest source can be found on [GitHub].  
Download the latest builds from [Jenkins].  [![Build Status](http://build.spout.org/job/Vanilla/badge/icon)][Jenkins]  
View the latest [Javadoc].

License
-------
Vanilla is licensed under the [GNU Lesser General Public License Version 3][License], but with a provision that files are released under the MIT license 180 days after they are published. Please see the `LICENSE.txt` file for details.

Compiling
---------
Vanilla uses Maven to handle its dependencies.

* Install [Maven 2 or 3](http://maven.apache.org/download.html)  
* Checkout this repo and run: `mvn clean install`

Using with Your Project
-----------------------
For those using [Maven](http://maven.apache.org/download.html) to manage project dependencies, simply include the following in your pom.xml:

    <dependency>
        <groupId>org.spout</groupId>
        <artifactId>vanilla</artifactId>
        <version>1.2.5-SNAPSHOT</version>
    </dependency>

If you do not already have repo.spout.org in your repository list, you will need to add this also:

    <repository>
        <id>spout-repo</id>
        <url>https://repo.spout.org</url>
    </repository>

Coding and Pull Request Formatting
----------------------------------
* Generally follow the Oracle coding standards.
* Use tabs, no spaces.
* No trailing whitespaces.
* 200 column limit for readability.
* All new files must include the license header. This can be done automatically with Maven by running mvn clean.
* All changes made via pull requests first be compiled locally to verify that the code does indeed compile, and tested to verify that it actually works.
* Where practical, a test should be included to verify the change. Except in exceptional cases, bug fixes **MUST** include a test case which fails for the current version and passes for the updated version.
* Commit messages must include:
    - A brief description of the change
    - A more detailed description of the change (second line and below, optional)
    - Sign-off, verifying agreement with the license terms
* Number of commits in a pull request should be kept to **one commit** and all additional commits must be **squashed** except for circumstantial exceptions.
* You may have more than one commit in a pull request if the commits are separate changes, otherwise squash the commits.
* For clarification, see the full pull request guidelines [here](http://spout.in/prguide).

**Please follow the above conventions if you want your pull request(s) accepted.**

[Project Logo]: http://cdn.spout.org/img/logo/vanilla_630x150.png
[License]: http://www.spout.org/SpoutDevLicenseV1.txt
[Website]: http://www.spout.org
[Forums]: http://forums.spout.org
[GitHub]: https://github.com/VanillaDev/Vanilla
[Javadoc]: http://jd.spout.org/vanilla/
[Jenkins]: http://build.spout.org/job/Vanilla
[Issues]: http://issues.spout.org/browse/Vanilla
[Twitter]: http://spout.in/twitter
[Twitter Logo]: http://cdn.spout.org/img/button/twitter_follow_us.png
[Facebook]: http://spout.in/facebook
[Facebook Logo]: http://cdn.spout.org/img/button/facebook_like_us.png
[Donate]: https://www.paypal.com/cgi-bin/webscr?hosted_button_id=QNJH72R72TZ64&item_name=Vanilla+donation+%28from+github.com%29&cmd=_s-xclick
[Donate Logo]: http://cdn.spout.org/img/button/donate_paypal_96x96.png
