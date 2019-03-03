TEEK: Taxonomy Extension system for Emerging Knowledge implementing the classifier trainer API of the athlete/trainer pattern. 

See teek.yml config file for metadata about the microservice. The same metadata can be accesses by calling /metadata of the running service. 

The Dockerfile to see which commands you need to run the service on a Linux machine with Java. 

Or you can just run the Docker container including everything necessary. 

Version change log:

- 0.0.1: building system from ntfc v1.1.1 implementing classifier-trainer API v 1.0.8
- 0.0.2: built trainer to simply encapsulate the configuration within the model. No need to spawn threads for that.
- 0.1.0: implemented suggestions resource
- 0.2.0: implemented category suggestions resource. Moved prediction method to core class. 
- 0.3.0: implemented relationship suggestions resoruce.