package com.example.notes

class NoteListItem (_name: String, _type: Int, _id: Int) {
    val name = _name
    val type = _type
    val id = _id
    //public val img = null

    fun getType() : String{
        return NoteType.typeToString[type]
    }

}

class NoteType {
    companion object {
        const val written = 0
        const val audio = 1
        const val drawing = 2
        val typeToString = arrayOf<String>("Text Note", "Audio Note", "Drawn Note")
    }
}