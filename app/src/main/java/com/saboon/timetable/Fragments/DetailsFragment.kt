package com.saboon.timetable.Fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saboon.timetable.Adapters.DetailsRecyclerAdapter
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.R
import com.saboon.timetable.Utils.IDGenerator
import com.saboon.timetable.ViewModels.DetailsViewModel
import com.saboon.timetable.databinding.FragmentDetailsBinding
import java.text.SimpleDateFormat
import java.util.*


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding?=null
    private val binding get() =_binding!!


    lateinit var viewModel: DetailsViewModel
    private val recyclerAdapter = DetailsRecyclerAdapter(arrayListOf())


    private lateinit var lessonID: String
    private lateinit var belowProgramID : String

    lateinit var createdLesson: ModelLesson

    //yeni kayit icinmi gelindi yoksa eski kayitmi gosteriliyor diye kotrol icin
    //checkTheChanges() fonksiyonu icin gerekli
    private var isNewLesson = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //eger geri tusuna basilirsa burasi calisir
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                //geri tusuna basilirsa degisiklikleri kontrol et
                checkTheChanges{response ->
                    if (response){
                        val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment(belowProgramID)
                        findNavController().navigate(actionToBack)
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_details, container, false)

        _binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        //viewModel.refreshData()

        arguments?.let {
            if(it != null){
                DetailsFragmentArgs.fromBundle(it).selectedLessonID?.let {
                    lessonID = it
                    isNewLesson = false
                    viewModel.getDataFromSQLite(it)
                    showButtons(false)
                } ?: run {
                    activateAddProgram(false)
                }

                DetailsFragmentArgs.fromBundle(it).belowProgramID?.let {
                    belowProgramID = it
                }

            }
        }

        binding.fragmentDetailsRecyclerViewProgramRecycler.layoutManager = LinearLayoutManager(context)

        binding.fragmentDetailsRecyclerViewProgramRecycler.adapter = recyclerAdapter




        binding.fragmentDetailsTextViewLessonDetails.setOnClickListener {
            checkTheChanges{response ->
                if (response){
                    val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment(belowProgramID)
                    it.findNavController().navigate(actionToBack)
                }
            }

        }

        binding.fragmentDetailsImageViewEdit.setOnClickListener {
            // TODO: duzenleme butonunu yap
        }


        binding.fragmentDetailsButtonSave.setOnClickListener{
            storeData()
            showButtons(false)
            activateAddProgram(true)
        }


        binding.fragmentDetailsTextViewAddProgram.setOnClickListener {

            if (lessonID != null || lessonID != "null"){
                val actionToAddProgram = DetailsFragmentDirections.actionDetailsFragmentToAddProgramFragment(null,lessonID, belowProgramID)
                it.findNavController().navigate(actionToAddProgram)
            }else{
                //show alert Dialog
            }

        }

        binding.fragmentDetailsImageViewDelete.setOnClickListener {

            val title = resources.getString(R.string.alertDialog_titleDelete)
            val message = resources.getString(R.string.alertDialog_sureDelete)

            showAlert(title,message){
                if(it){
                    viewModel.deleteLesson(lessonID)
                    Toast.makeText(context,resources.getString(R.string.toast_successful),Toast.LENGTH_LONG).show()
                    val actionToMain = DetailsFragmentDirections.actionDetailsFragmentToMainFragment(belowProgramID)
                    findNavController().navigate(actionToMain)
                }
            }
        }

        observeLiveData()

    }


    fun storeData(){
        val lessonName = binding.fragmentDetailsEditTextLessonName.text.toString()
        val id = IDGenerator().generateLessonID(lessonName)
        val dateAdded = SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(Calendar.getInstance().time)
        val lecturerName = binding.fragmentDetailsEditTextLecturerName.text.toString()
        val color = "#C62910"
        val absenteeism = "2"
        val belowProgram = belowProgramID

        createdLesson = ModelLesson(id,dateAdded,lessonName,lecturerName,color,absenteeism,belowProgram)
        lessonID = createdLesson.id

        viewModel.storeLessonInDatabase(createdLesson)
    }


    fun observeLiveData(){
        viewModel.lessonName.observe(viewLifecycleOwner, Observer {
            binding.fragmentDetailsEditTextLessonName.setText(it)
        })
        viewModel.lecturerName.observe(viewLifecycleOwner, Observer {
            binding.fragmentDetailsEditTextLecturerName.setText(it)
        })
        viewModel.programTimes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.fragmentDetailsRecyclerViewProgramRecycler.visibility = View.VISIBLE
                recyclerAdapter.updateList(it)
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it){
                binding.fragmentDetailsRecyclerViewProgramRecycler.visibility = View.GONE
                binding.detailsLoadingProgressBar.visibility = View.VISIBLE
                binding.detailsErrorText.visibility = View.GONE
                binding.detailsEmptyText.visibility = View.GONE
            }else{
                binding.detailsLoadingProgressBar.visibility = View.GONE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.fragmentDetailsRecyclerViewProgramRecycler.visibility = View.GONE
                    binding.detailsLoadingProgressBar.visibility = View.GONE
                    binding.detailsErrorText.visibility = View.VISIBLE
                    binding.detailsEmptyText.visibility = View.GONE
                }else{
                    binding.detailsErrorText.visibility = View.GONE
                }
            }
        })

        viewModel.empty.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.fragmentDetailsRecyclerViewProgramRecycler.visibility = View.GONE
                    binding.detailsLoadingProgressBar.visibility = View.GONE
                    binding.detailsErrorText.visibility = View.GONE
                    binding.detailsEmptyText.visibility = View.VISIBLE
                }else{
                    binding.detailsEmptyText.visibility = View.GONE
                }
            }
        })


    }

    fun showButtons(state: Boolean){
        if(state){
            binding.fragmentDetailsLinearLayoutButtons.visibility = View.VISIBLE
            //binding.fragmentDetailsEditTextLessonName.isFocusable = true
            //binding.fragmentDetailsEditTextLecturerName.isFocusable = true
        }else{
            binding.fragmentDetailsLinearLayoutButtons.visibility = View.GONE
            //binding.fragmentDetailsEditTextLessonName.isFocusable = false
            //binding.fragmentDetailsEditTextLecturerName.isFocusable = false
        }
    }

    fun showAlert(title: String,message: String, response: (Boolean) -> Unit){

        val alertDialogBuilder = AlertDialog.Builder(activity)

        val string = requireActivity().resources

        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setPositiveButton(string.getString(R.string.alertDialog_posButton)) { dialog, id ->
            response(true)
        }
        alertDialogBuilder.setNegativeButton(string.getString(R.string.alertDialog_negButton)){dialog, id ->
            response(false)
        }

        alertDialogBuilder.show()
    }

    fun activateAddProgram(state: Boolean){
        if (state){
            binding.fragmentDetailsLinearLayoutAddProgram.isEnabled = true
        }else{
            binding.fragmentDetailsLinearLayoutAddProgram.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }





    fun checkTheChanges(response: (Boolean) -> Unit){

        //burasi kullanici degisiklik yapip yapmadigini kotrol eder
        //eger yapmis ise kullaniciya degisiklikleri kaydetmek isteyip istemedigi sorulur
        //kullanici evet yanitini verirse veri tabani degisen degerler ile guncellenir
        if (!isNewLesson){
            val newLessonName = binding.fragmentDetailsEditTextLessonName.text.toString()
            val newLecturerName = binding.fragmentDetailsEditTextLecturerName.text.toString()
            val newColor = "#C65910"
            val newAbsenteeism = "0"

            viewModel.getLessonFromSQLite(lessonID){
                if (it.lessonName != newLessonName || it.lecturerName != newLecturerName /*|| it.color != newColor || it.absenteeism != newAbsenteeism*/){
                    showAlert("kaydet","degisiklikleri kaydetmek istiyormusunuz"){ wantSave ->
                        if(wantSave){
                            val updatedLesson = ModelLesson(
                                it.id,
                                it.id,
                                newLessonName,
                                newLecturerName,
                                it.color,
                                it.absenteeism,
                                it.belowProgram
                            )
                            viewModel.updateLesson(updatedLesson){
                                if (it){
                                    //eger kayit basarili ise true gonder
                                    response(true)
                                }else{
                                    //eger kayit basarisiz ise false gonder
                                    response(false)
                                }
                            }
                        }
                    }
                }else{
                    //eger kayit yapilmayacaksa true gonder
                    response(true)
                }
            }
        }else{
            //eger kayit yapilmayacaksa true gonder
            response(true)
        }
    }


}