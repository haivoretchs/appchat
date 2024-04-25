package com.remotetechs.appchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter:RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var listUser:MutableList<String> = mutableListOf()
    fun setListUser(listUser:MutableList<String>){
        this.listUser=listUser
        notifyItemRangeChanged(0,listUser.size)
    }
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        private val tvUser: TextView =itemView.findViewById(R.id.tv_user)

/*        fun bind(item:User){
            tvUser.text=item.userName
        }*/
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      /*  holder.bind(listUser[position])*/
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}