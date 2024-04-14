# Sample-tracker


## Environment Setup

To get started with the code initially you may need to install the appropriate
JDK and javafx openjfx versions.

Open the project in intellij (Currently 2024.1 (Community Edition))
from there you need to set up the configurations to run it.
> ### 1. Set up Install Configuration
> - On the title bar Run -> Edit Configurations...
> - Click the + and add a new Maven configuration
> - Name it SampleTracker
> - Under the Run option press the insert Maven command button
> - Select Install under commands
> - press Apply  

> ### 2. Set up Application Configuration
> - Click the + again and add an application configuration
> - Name it TrackerGUI and set the main class org.openjfx.TrackerGUI
> - Open Modify options and choose Add before launch task
> - under Add new Task choose "Run another configuration"
> - Choose our maven install task we just created
> - Hit Apply and Ok and test it out. the program should launch.
> - If you edit in vs code and you find the project sometimes won't build
> - then you may need to add the following flow of maven tasks:
>> - clean
>> - compile
>> - install

> ### 3. Build Artifact
> We need a jar artifact to be built in order to create an installer for
> our application at some point. The needed Project settings should 
> already exist  
> - on the title bar Build -> Build Artifacts...
> - on the context menu that opens: SampleTracker:jar -> Rebuild
> #### This will fail if we don't previously run maven install

> ### Manually install
> We can manually run our Maven commands if we need to. On the right of the 
> screen there should be a tiny Maven logo that opens an imbedded window.
> If it isn't present, enable it from View -> tool window.
> Under our project name -> Licecycle we can run our package and install
> commands as needed.