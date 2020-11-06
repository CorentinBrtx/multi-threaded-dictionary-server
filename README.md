# multi-threaded-dictionary-server

A small Java application allowing users to access a dictionary from different computers and to modify it, all the computations being done in separated threads.

To use the application, a server must be launched using the `src/server/model/DictionaryServer.java` file. Then, multiple users can connect to this server using the `src/client/model/DictionaryClient.java`.
