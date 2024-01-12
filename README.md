## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

## Guildline

### Start mail server

* **Step 1:** `cd mail-user-agent\server`

* **Step 2:**
    * Java version < 11: `java -jar test-mail-server-1.0.jar -s 2225 -p 3335 -m ./`
    * Java version >= 11: `java --module-path "path\to\javafx\javafx-sdk-xx\lib" --add-modules javafx.controls,javafx.fxml -jar test-mail-server-1.0.jar -s 2225 -p 3335 -m ./`

### Run client app by using .jar

* **Step 1:** Go to `/src`

* **Step 2:** Open `mail-user-agent.jar`

### Run client app by command

**MacOS/Linux:**

* **Step 1:** `cd \mail-user-agent\src`

* **Step 2:** `./compile.sh`

* **Step 3:** `./run.sh`

**Window:**

* **Step 1:** `cd \mail-user-agent\src`

* **Step 2:** `./compile.ps1`

* **Step 3:** `./run.ps1`