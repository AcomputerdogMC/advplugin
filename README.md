# advplugin - framework for Bukkit plugins that require advanced functionality

ADVPlugin is a lightweight framework to simplify complex tasks that are not aided by the Bukkit or Spigot APIs but often necessary in large or complex plugins.
It provides features like automatic database setup, command permission and arguments checking, and easier exception logging.  A sample plugin is included to demonstrate the usage of most features.

#### Core

Most features are available in the "core" version of the library, but anything that requires a large external dependency is excluded.  Notably, database access is only included in the "full" version.

Features:

* ALogger - wrapper around existing bukkit logging that provides more log levels and does provides utility methods to log messages at a specified level.
* AsyncTask - easily perform slow or intense actions in another thread and the pass the result back to the game thread for use with bukkit APIs.
* CommandLayer - offload the rote permissions and arguments checking of command handling and only worry about the command implementation.

#### Full

The full build of advplugin includes the core build + additional features that require external dependencies.

Features:

* DbLayer - automatically connect to a database, intialize data objects, and create any needed tables.  Uses ORMLite.
