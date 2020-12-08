package com.example.pinatafarm.presentation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinatafarm.R
import com.example.pinatafarm.domain.Person
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File

class ListFragment : Fragment(R.layout.fragment_list), ActionListener {

    private val viewModel: SharedViewModel by sharedViewModel()

    private val personsAdapter = PersonsAdapter(this)
    private lateinit var loadingView: ProgressBar
    private lateinit var personsView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingView = view.findViewById(R.id.loadingView)
        personsView = view.findViewById<RecyclerView>(R.id.personList).apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = personsAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getPeopleUiState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UiState.Loading -> loadingView.visibility = View.VISIBLE
                is UiState.Error -> Toast.makeText(requireContext(), it.e.toString(), Toast.LENGTH_SHORT).show()
                is UiState.Result -> {
                    loadingView.visibility = View.GONE
                    personsAdapter.update(it.persons)
                }
            }
        })
    }

    override fun onItemClicked(item: Person) {
        viewModel.setSelected(item)
    }
}

class PersonsAdapter(private val actionListener: ActionListener) :
    RecyclerView.Adapter<PersonsAdapter.ItemHolder>() {
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            person: Person,
            actionListener: ActionListener
        ) {
            itemView.findViewById<TextView>(R.id.personName).apply {
                text = person.name
            }
            itemView.findViewById<ImageView>(R.id.thumbnail).apply {
                Glide.with(this).load(Uri.fromFile(File("//android_asset/img/${person.img}"))).into(this)
            }
            itemView.setOnClickListener {
                actionListener.onItemClicked(person)
                it.findNavController().navigate(R.id.action_listFragment_to_detailFragment)
            }
        }
    }

    var persons = listOf<Person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int = persons.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(persons[position], actionListener)
    }

    fun update(items: List<Person>) {
        DiffUtil.calculateDiff(DiffCallback(persons, items)).dispatchUpdatesTo(this)
        persons = items
    }
}

interface ActionListener {
    fun onItemClicked(item: Person)
}

class DiffCallback(private val oldList: List<Person>, private val newList: List<Person>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}