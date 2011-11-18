package com.ps.sr.model

import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * This represents an item that a learner could interact with.
 *
 * i.e. "{text: 'The sky is blue', icon: 'blue_sky.png', type: 'RSP'}"(The learner would be asked to repeat 'The sky is blue' and
 * an icon of blue_sky.png would be used as auxiliary information pertaining to the text.
 */
class LevelItem {

    String text
    String icon

	boolean equals(other) {
		if (!(other instanceof LevelItem)) {
			return false
		}

		other?.id == id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (text) builder.append(text)
		if (id) builder.append(id)
		builder.toHashCode()
	}

    static belongsTo = [level:Level]

    static constraints = {
    }

    String toString(){
        return text
    }
}
