package com.gavincode.bujo.presentation.ui.bullet

import android.view.LayoutInflater
import android.widget.FrameLayout
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.ui.widget.BulletEditText
import com.gavincode.checklistview.ChecklistData
import com.gavincode.checklistview.ChecklistView

class ContentViewHelper {
    private var contentView: ContentViewInterface? = null

    fun setContentView(contentViewInterface: ContentViewInterface) {
        this.contentView = contentViewInterface
    }

    fun setData(context: String) {
        contentView?.setData(context)
    }

    fun getData(): String = contentView?.getData() ?: ""
}

class PlainContentView(container: FrameLayout): ContentViewInterface {

    private val contentView: BulletEditText

    init {
        container.removeAllViews()
        val view = LayoutInflater.from(container.context).inflate(R.layout.view_bullet_content, null, false)
        contentView = view.findViewById(R.id.bullet_content_edit_text)
        container.addView(view)
    }

    override fun getData(): String {
        return contentView.text.toString()
    }

    override fun setData(string: String) {
        contentView.setText(string)
    }

}

class ListContentView(container: FrameLayout): ContentViewInterface {

    private val contentView: ChecklistView

    init {
        container.removeAllViews()
        val view = LayoutInflater.from(container.context).inflate(R.layout.view_check_list, null, false)
        contentView = view.findViewById(R.id.check_list_view)
        container.addView(view)
//        container.layoutResource = R.layout.view_check_list
//        val view = container.inflate()
//        contentView = view.findViewById(R.id.check_list_view)
    }

    override fun getData(): String {
        val stringBuffer = StringBuffer()
        val list = contentView.getChecklistData()
        for (checklistData in list) {
            stringBuffer.append("[")
            if (checklistData.checked) stringBuffer.append("X")
            stringBuffer.append("]")
            stringBuffer.append(checklistData.text).append("\n")
        }
        return stringBuffer.toString()
    }

    override fun setData(string: String) {
        val array = string.split("\n")
        val list = mutableListOf<ChecklistData>()
        for (text in  array) {
            var isCheck: Boolean = false
            var textString: String
            if (text.startsWith("[X]")) {
                isCheck = true
                textString = text.substring(3)
            } else if (text.startsWith("[]")) {
                textString = text.substring(2)
            } else {
                textString = text
            }
            list.add(ChecklistData(textString, isCheck))
        }
        contentView.setChecklistData(list)
    }

}