package com.ps.sr.model

import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * This represents a level of achievement or difficulty in some cases.  This will contain a collection of message items
 * we could use to interact with a learner.
 *
 * i.e. "Learn English Level 1", "Learn English Easy"
 */
class Level {

    List levelItems

	boolean equals(other) {
		if (!(other instanceof Level)) {
			return false
		}

		other?.id == id &&
                other.program?.id == program?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (name) builder.append(name)
		if (id) builder.append(id)
		builder.toHashCode()
	}

    static belongsTo = [program: Program]
    static hasMany = [levelItems: LevelItem]

    static mapping = {
        levelItems indexColumn:[name:"level_item_index", type:LevelItem]
    }

    String name
    String description

    static constraints = {levelItems()
    }

    String toString(){
        return name
    }
}
