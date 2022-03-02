package com.saboon.timetable.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saboon.timetable.Adapters.MainRecyclerAdapter
import com.saboon.timetable.ViewModels.MainViewModel
import com.saboon.timetable.databinding.FragmentMainBinding


class MainFragment : Fragment() {





    private var _binding: FragmentMainBinding?=null

    private val binding get() = _binding!!


    lateinit var viewModel: MainViewModel
    private val recyclerAdapter = MainRecyclerAdapter(arrayListOf(), arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)

        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fragmentMainImageViewAdd.setOnClickListener {
            val actionToAddLesson = MainFragmentDirections.actionMainFragmentToDetailsFragment(null)
            it.findNavController().navigate(actionToAddLesson)
        }

        binding.fragmentMainTextViewProgramName.setOnClickListener{
            showFragmentDialog()
        }



        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.refreshData("belowID")

        binding.fragmentMainRecyclerViewLessonsRecycler.layoutManager = LinearLayoutManager(context)
        binding.fragmentMainRecyclerViewLessonsRecycler.adapter = recyclerAdapter
        binding.fragmentMainRecyclerViewLessonsRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        observeData()

    }


    fun showFragmentDialog(){
        val fragmentManager = parentFragmentManager
        val newFragment = ChooseProgramDialog()
        newFragment.show(fragmentManager,"dialog")
    }



    fun observeData(){

        viewModel.lessonList.observe(viewLifecycleOwner, Observer {lessonList ->
            lessonList?.let {
                viewModel.lessonTimeList.observe(viewLifecycleOwner, Observer {timeList ->
                    timeList?.let {
                        recyclerAdapter.updateList(lessonList, timeList)
                        binding.fragmentMainRecyclerViewLessonsRecycler.visibility = View.VISIBLE
                        binding.mainErrorText.visibility = View.GONE
                        binding.mainLoadingProgressBar.visibility = View.GONE
                    }
                })
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.fragmentMainRecyclerViewLessonsRecycler.visibility = View.GONE
                    binding.mainLoadingProgressBar.visibility = View.VISIBLE
                    binding.mainErrorText.visibility = View.GONE
                    binding.mainEmptyText.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.fragmentMainRecyclerViewLessonsRecycler.visibility = View.GONE
                    binding.mainLoadingProgressBar.visibility = View.GONE
                    binding.mainErrorText.visibility = View.VISIBLE
                    binding.mainEmptyText.visibility = View.GONE
                }
            }
        })
        viewModel.empty.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.fragmentMainRecyclerViewLessonsRecycler.visibility = View.GONE
                    binding.mainLoadingProgressBar.visibility = View.GONE
                    binding.mainErrorText.visibility = View.GONE
                    binding.mainEmptyText.visibility = View.VISIBLE
                }
            }
        })



    }




    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}