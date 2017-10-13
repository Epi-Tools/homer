'use strict'

class ProfileController {

    * index (request, response) {
        yield response.sendView('profile')
    }

}

module.exports = ProfileController
