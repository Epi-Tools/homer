'use strict'

class AboutController {

    * index (request, response) {
        yield response.sendView('about')
    }

}

module.exports = AboutController
