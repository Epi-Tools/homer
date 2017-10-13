'use strict'

/*
|--------------------------------------------------------------------------
| Model and Database Factory
|--------------------------------------------------------------------------
|
| Factories let you define blueprints for your database models or tables.
| These blueprints can be used with seeds to create fake entries. Also
| factories are helpful when writing tests.
|
*/

const Factory = use('Factory')

/*
|--------------------------------------------------------------------------
| User Model Blueprint
|--------------------------------------------------------------------------
| Below is an example of blueprint for User Model. You can make use of
| this blueprint inside your seeds to generate dummy data.
|
*/
/*
Factory.blueprint('App/Model/User', (fake) => {
  return {
    username: fake.username(),
    email: fake.email(),
    password: fake.password()
  }
})
*/
Factory.blueprint('App/Model/Project', fake => ({
  name: fake.name(),
  github: fake.url(),
  presentation: fake.url(),
  description: fake.paragraph(),
  followUp1: fake.paragraph(),
  followUp2: fake.paragraph(),
  delivery: fake.paragraph(),
  dateFollowUp1: fake.date({ string: true }),
  dateFollowUp2: fake.date({ string: true }),
  dateDelivery: fake.date({ string: true }),
  spices: 0,
  askedSpices: fake.integer({ min: 60, max: 840 }),
  members: JSON.stringify([ fake.name(), fake.name() ]),
  state: 0,
  validate: fake.bool(),
  userId: 1
}))