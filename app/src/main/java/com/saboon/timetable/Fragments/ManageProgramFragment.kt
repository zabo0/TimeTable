package com.saboon.timetable.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saboon.timetable.Adapters.ManageProgRecyclerAdapter
import com.saboon.timetable.Models.ModelProgram
import com.saboon.timetable.R
import com.saboon.timetable.Utils.IDGenerator
import com.saboon.timetable.ViewModels.ManageProgViewModel
import com.saboon.timetable.databinding.FragmentManageProgramBinding
import java.text.SimpleDateFormat
import java.util.*


class ManageProgramFragment : Fragment() {

    private val SHARED_PREF_PROG_ID = "progID"
    private val SHARED_PREF_OLD_PROG_ID = "oldProgID"
    private lateinit var sharedPref: SharedPreferences

    private var _binding: FragmentManageProgramBinding? = null
    private val binding get() = _binding!!


    lateinit var viewModel: ManageProgViewModel
    private val recyclerAdapter = ManageProgRecyclerAdapter(arrayListOf())


    private lateinit var currentProgramID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        sharedPref.getString(SHARED_PREF_PROG_ID, null)?.let {
            val actionToMain = ManageProgramFragmentDirections.actionManageProgramFragmentToMainFragment(it)
            findNavController().navigate(actionToMain)
        }

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

        binding.fragmentManageProgRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.fragmentManageProgRecyclerView.adapter = recyclerAdapter
        binding.fragmentManageProgRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        binding.fragmentManageProgImageViewAdd.setOnClickListener{
            alerDialog()

        }

        binding.fragmentManageProgTextViewManagePrograms.setOnClickListener{
            currentProgramID = sharedPref.getString(SHARED_PREF_OLD_PROG_ID, null).toString()
            if(currentProgramID != "null"){
                val actionToBack = ManageProgramFragmentDirections.actionManageProgramFragmentToMainFragment(currentProgramID)
                it.findNavController().navigate(actionToBack)
            }else{
                Toast.makeText(context,"Program secmelisin",Toast.LENGTH_LONG)
            }
        }


        observeData()
    }


    fun observeData(){

        viewModel.programs.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.fragmentManageProgRecyclerView.visibility = View.VISIBLE
                recyclerAdapter.updateList(it)
                binding.manageLoadingProgressBar.visibility = View.GONE
                binding.manageEmptyText.visibility = View.GONE
                binding.manageErrorText.visibility = View.GONE
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.fragmentManageProgRecyclerView.visibility = View.GONE
                    binding.manageLoadingProgressBar.visibility = View.VISIBLE
                    binding.manageEmptyText.visibility = View.GONE
                    binding.manageErrorText.visibility = View.GONE
                }else{
                    binding.manageLoadingProgressBar.visibility = View.GONE
                }
            }
        })
        viewModel.empty.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.fragmentManageProgRecyclerView.visibility = View.GONE
                    binding.manageLoadingProgressBar.visibility = View.GONE
                    binding.manageEmptyText.visibility = View.VISIBLE
                    binding.manageErrorText.visibility = View.GONE
                }else{
                    binding.manageEmptyText.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    binding.fragmentManageProgRecyclerView.visibility = View.GONE
                    binding.manageLoadingProgressBar.visibility = View.GONE
                    binding.manageEmptyText.visibility = View.GONE
                    binding.manageErrorText.visibility = View.VISIBLE
                }else{
                    binding.manageErrorText.visibility = View.GONE
                }
            }
        })
    }



    fun alerDialog(){

        val alertDialogBuilder = AlertDialog.Builder(activity)

        val inflater = requireActivity().layoutInflater
        val dialogLayout = inflater.inflate(R.layout.manage_dialog_view, null)
        val programNameText = dialogLayout.findViewById<EditText>(R.id.dialog_progName)

       with(alertDialogBuilder){
           setTitle("Add Program")
           setPositiveButton("Save"){dialog, which ->
               val programName = programNameText.text.toString()
               val dateAdded = SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(Calendar.getInstance().time)
               val dateEdited = SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(Calendar.getInstance().time)
               val id = IDGenerator().generateProgramID(programName)
               val newProgram = ModelProgram(id,programName,dateAdded,dateEdited)
               viewModel.storeProgramInDatabase(newProgram){callback ->
                   if (callback){
                       viewModel.getAllProgramsFromDatabase()
                   }
               }

           }
           setNegativeButton("Cancel") {dialog, which ->
                dialog.cancel()
           }
           setView(dialogLayout)
           show()
       }
    }


}