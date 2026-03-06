# AGENTS.md

## Project overview
Monolithic REST service to support API operations in the backend side, focused for a travel agency.
Handles:
- Public landing page with a brief presentation, contact information, services, promotions, quotation request form, blogs and a login to use the app.
- App used by the travel agent to manage their clients, providers, commissions, expenses, promotions and blogs
- App includes admin user with an administration module: users, roles, permissions, audit
- FrontEnd (React) code is included in a different project

## Code style guidelines
- Variables and methods use camelCase as java naming convention
- Collections, lists or sets, use objectList naming instead of objects in plural
- Methods should have java documentation
- Messages for the agent are in Latin Spanish, Code and documentation is in English language

## Testing instructions
- Project with unit test case for each method
- Find the CI plan in the .github/workflows folder
- Find the db schema in postgres/init/01_schema.sql file

## Security considerations
- Some methods use jwt strategy for the app internally, other methods are used for the public landing page
- Never send tokens, passwords or secrets to the repository public