package com.saboon.timetable.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.saboon.timetable.Adapters.ManageProgRecyclerAdapter
import com.saboon.timetable.R
import com.saboon.timetable.ViewModels.ManageProgViewModel
import com.saboon.timetable.databinding.FragmentManageProgramBinding


class ManageProgramFragment : Fragment() {


    private var _binding: FragmentManageProgramBinding? = null
    private val binding get() = _binding!!


    lateinit var viewModel: ManageProgViewModel
    private val recyclerAdapter = ManageProgRecyclerAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_manage_program, container, false)

        _binding = FragmentManageProgramBinding.inflate(inflater,container,false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ManageProgViewModel::class.java)
        viewModel.getAllProgramsFromDatabase()



        binding.fragmentManageProgImageViewAdd.setOnClickListener{



        }


        observeData()


    }


    fun observeData(){

        viewModel.programs.observe(viewLifecycleOwner, Observer {
            it?.let {
                recyclerAdapter.updateList(it)
                binding.fragmentMangeProgRecyclerView.visibility = View.VISIBLE
                binding.manageLoadingProgressBar.visibility = View.GONE
                binding.manageEmptyText.visibility = View.GONE
                binding.manageErrorText.visibility = View.GONE
            }.also {
                binding.fragmentMangeProgRecyclerView.visibility = View.GONE
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.fragmentMangeProgRecyclerView.visibility = View.GONE
                binding.manageLoadingProgressBar.visibility = View.VISIBLE
                binding.manageEmptyText.visibility = View.GONE
                binding.manageErrorText.visibility = View.GONE
            }.also {
                binding.manageLoadingProgressBar.visibility = View.GONE
            }
        })
        viewModel.empty.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.fragmentMangeProgRecyclerView.visibility = View.GONE
                binding.manageLoadingProgressBar.visibility = View.GONE
                binding.manageEmptyText.visibility = View.VISIBLE
                binding.manageErrorText.visibility = View.GONE
            }.also {
                binding.manageEmptyText.visibility = View.GONE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.fragmentMangeProgRecyclerView.visibility = View.GONE
                binding.manageLoadingProgressBar.visibility = View.GONE
                binding.manageEmptyText.visibility = View.GONE
                binding.manageErrorText.visibility = View.VISIBLE
            }.also {
                binding.manageErrorText.visibility = View.GONE
            }
        })


    }


}