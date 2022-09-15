# Danger

This repository contains a Dangerfile containing branch naming, commit, and other code rules that can be used to run inside your continuous integration (CI) environment.

Note: This can only be used when JaCoCo coverage reports have been generated.

# How to Use

1. Define environment variables
    ```
    # List of modules to locate path of JaCoCo coverage results
    DANGER_MODULES=module1,module2
    # Ticket number regex
    DANGER_TICKET_NUM_REGEX=(ABC|DEFG)-\d{3,5}
    ```
1. Run Danger
    ```
    bundle install
    bundle exec danger --dangerfile=<path to Dangerfile>
    ```

## Resources
- [Danger](https://danger.systems/guides/what_does_danger_do.html)
- [Dangerfile](https://danger.systems/guides/dangerfile.html)
