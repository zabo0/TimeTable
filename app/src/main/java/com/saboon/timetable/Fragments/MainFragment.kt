package com.saboon.timetable.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saboon.timetable.Adapters.MainRecyclerAdapter
import com.saboon.timetable.Utils.SharedPref
import com.saboon.timetable.ViewModels.MainViewModel
import com.saboon.timetable.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding?=null

    private val binding get() = _binding!!


    lateinit var viewModel: MainViewModel
    private val recyclerAdapter = MainRecyclerAdapter(arrayListOf(), arrayListOf())

    private lateinit var currentProgramID: String



    //recyclerviewde saga ve sola kaydirma islemleri icin
    val swipeCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val lesson = viewModel.lessonList.value
            lesson?.get(position)?.id

            when( direction){
                ItemTouchHelper.LEFT -> {
                    //devamsizligi bir arttir
                }
                ItemTouchHelper.RIGHT -> {
                    //devamsizligi bir azalt
                }
            }
        }


    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //sharedPrefe kaydedilen son program getirilir
        SharedPref(requireActivity()).getIDFromSharedPref {
            if (it != "null"){
                currentProgramID = it
            }else{
                val actionToManageProgram = MainFragmentDirections.actionMainFragmentToManageProgramFragment()
                findNavController().navigate(actionToManageProgram)
            }
        }

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

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.refreshData(currentProgramID)


        binding.fragmentMainImageViewAdd.setOnClickListener {
            val actionToAddLesson = MainFragmentDirections.actionMainFragmentToDetailsFragment(null, currentProgramID)
            it.findNavController().navigate(actionToAddLesson)
        }

        binding.fragmentMainTextViewProgramName.setOnClickListener{
            val actionToManageProgram = MainFragmentDirections.actionMainFragmentToManageProgramFragment()
            it.findNavController().navigate(actionToManageProgram)
        }

        binding.fragmentMainRecyclerViewLessonsRecycler.layoutManager = LinearLayoutManager(context)
        binding.fragmentMainRecyclerViewLessonsRecycler.adapter = recyclerAdapter
        //ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.fragmentMainRecyclerViewLessonsRecycler)
        binding.fragmentMainRecyclerViewLessonsRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        observeData()

    }






    fun observeData(){


        viewModel.programName.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.fragmentMainTextViewProgramName.setText(it)
            }
        })

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
                }else{
                    binding.mainLoadingProgressBar.visibility = View.GONE
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
                }else{
                    binding.mainErrorText.visibility = View.GONE
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
                }else{
                    binding.mainEmptyText.visibility = View.GONE
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }




}