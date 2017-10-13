'use strict'

const Schema = use('Schema')

class ProjectsTableSchema extends Schema {

  up () {
    this.table('projects', table => {
        table.integer('userId')
    })
  }

  down () {
    this.table('projects', table => {
      // opposite of up goes here
    })
  }

}

module.exports = ProjectsTableSchema
