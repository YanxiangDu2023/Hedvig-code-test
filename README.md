# Hedvig Policy Manager

You are to build a very basic [insurance policy system](https://en.wikipedia.org/wiki/Insurance_policy), which holds
insurance contracts signed between an insurance provider (Hedvig) and a policyholder (end-users).
For simplicity, we will also be dealing with a stripped-down version of home insurance.

Since this is a code test, we skip important bits such as authentication and user management, and instead will focus
entirely on designing of the APIs and data model of the insurance policies.

If you feel anything is unclear or underspecified, make assumptions and explain your thinking during the interview.

### Use of AI

You are free to use AI during the assignment, especially to research and learn tools you might not yet be familiar with.

## Getting started

This repo is already set up as a launchable Spring Boot application with enough dependencies to build HTTP endpoints.
The maven `pom.xml` file contains three suggestions for database libraries, where you get to choose the one you are
most comfortable with (or excited to try).

## Requirements

### Data model

To create a valid insurance `Policy`, the following data points need to be collected:

* `personalNumber (string)` - the personal identity number of the policyholder 
* `address (string)` - the street address of the home, like "Kungsgatan 16" 
* `postalCode (string)` - the postal code of the home, like "11135"
* `startDate (date)` - the day this policy begins

The system should then calculate a monthly `premium` associated with the policy. You can choose whichever premium
calculation strategy you wish — the sky is the limit!

#### Insurance timeline

Typically, insurances can be updated by the policyholder with a new `startDate` and updated information — creating
a timeline of multiple back-to-back policies. You can think of this timeline as one `Insurance`, which holds multiple
`Policy` entries.

You can assume that `personalNumber` never changes between policies inside a single insurance.

### Starting an insurance

There should be an endpoint where insurances can be started by providing enough information to create a first policy.

### Changing your insurance

Policyholders typically need to change their insurance sometimes. For instance, if they decide to move from one
home to another at a given date. This should effectively put an end date on their existing policy and start a new one.

This endpoint should target a single insurance and create a new policy within it.

### Reading insurance and policies

There should be ways of reading policies back from the system. Here are some typical use cases for how one typically
queries the insurances:

* List all `Insurances` for a given `personalNumber`
* List all `Policies` for a given `personalNumber` on a specific `date`
* For a given `Insurance`, show its policy on a specific `date`

## Testing

Feel free to add tests for your system, and write them in the way you feel brings the most value.
