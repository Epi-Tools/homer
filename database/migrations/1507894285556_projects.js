'use strict'

const Schema = use('Schema')

class ProjectsTableSchema extends Schema {

  up () {
    this.create('projects', table => {
      table.increments()
      table.string('name')
      table.text('github')
      table.text('presentation') //ppt url
      table.text('description')
      table.text('followUp1')
      table.text('followUp2')
      table.text('delivery')
      table.date('dateFollowUp1')
      table.date('dateFollowUp2')
      table.date('dateDelivery')
      table.integer('spices')
      table.text('members')
      table.integer('state')
      table.boolean('validate')
      table.timestamps()
    })
  }

  down () {
    this.drop('projects')
  }

}

module.exports = ProjectsTableSchema
