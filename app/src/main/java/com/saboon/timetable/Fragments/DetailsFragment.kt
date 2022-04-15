package com.saboon.timetable.Fragments

import android.app.AlertDialog
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
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


    //ders icerisine program (time) eklenmismi diye kotrol eder
    private var isEmpty = true


    lateinit var createdLesson: ModelLesson

    //yeni kayit icinmi gelindi yoksa eski kayitmi gosteriliyor diye kotrol icin
    //checkTheChanges() fonksiyonu icin gerekli
    private var isNewLesson = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //eger geri tusuna basilirsa(telefondan) burasi calisir
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onBackPressed()
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


        arguments?.let {
            if(it != null){
                DetailsFragmentArgs.fromBundle(it).belowProgramID?.let {
                    belowProgramID = it
                }

                DetailsFragmentArgs.fromBundle(it).selectedLessonID?.let {
                    lessonID = it
                    isNewLesson = false
                    viewModel.getDataFromSQLite(it)
                }?: run{
                    //eger yeni bir ders ekleniyorsa burayi calistir
                    //createNewLesson()
                    binding.fragmentDetailsImageViewDelete.visibility = View.GONE
                    binding.fragmentDetailsImageViewAddNewLesson.visibility = View.VISIBLE
                    binding.fragmentDetailsImageVieCancelAddNewLesson.visibility = View.VISIBLE
                    binding.fragmentDetailsTextViewAddProgram.visibility = View.GONE
                }


            }
        }

        binding.fragmentDetailsRecyclerViewProgramRecycler.layoutManager = LinearLayoutManager(context)

        binding.fragmentDetailsRecyclerViewProgramRecycler.adapter = recyclerAdapter




        binding.fragmentDetailsTextViewLessonDetails.setOnClickListener {
            onBackPressed()
        }

        binding.fragmentDetailsTextViewAddProgram.setOnClickListener {

            if (lessonID != "null"){
                val actionToAddProgram = DetailsFragmentDirections.actionDetailsFragmentToAddProgramFragment(null,lessonID, belowProgramID)
                it.findNavController().navigate(actionToAddProgram)
            }else{
                //show alert Dialog
            }

        }

        binding.fragmentDetailsEditTextLessonName.setOnClickListener {
            val currentLessonName = binding.fragmentDetailsEditTextLessonName.text.toString()
            showChangeValueAlert("Edit","Change Lesson Name", currentLessonName){newLessonName->
                if (newLessonName != "null"){
                    binding.fragmentDetailsEditTextLessonName.setText(newLessonName)
                    binding.fragmentDetailsTextFieldLessonName.isErrorEnabled = false
                    //viewModel.updateLessonName(lessonID, newLessonName)
                }
            }
        }

        binding.fragmentDetailsEditTextLecturerName.setOnClickListener {
            val currentLecturerName = binding.fragmentDetailsEditTextLecturerName.text.toString()
            showChangeValueAlert("edit", "change Lecturer Name", currentLecturerName){newLecturerName->
                if(newLecturerName != "null"){
                    binding.fragmentDetailsEditTextLecturerName.setText(newLecturerName)
                    //viewModel.updateLecturerName(lessonID, newLecturerName)
                }
            }
        }

        binding.fragmentDetailsImageViewAddNewLesson.setOnClickListener {

            val lessonName = binding.fragmentDetailsEditTextLessonName.text.toString()

            if (lessonName != ""){
                val dateAdded = SimpleDateFormat("dd.MM.yyyy-HH:mm:ss").format(Calendar.getInstance().time)
                val lecturerName = binding.fragmentDetailsEditTextLecturerName.text.toString()
                val defaultColor = "#C62910"
                val defaultAbsenteeism = "0"
                val belowProgram = belowProgramID

                val programName = belowProgramID.split("_")[0]
                val id = IDGenerator().generateLessonID(programName, lessonName)

                createdLesson = ModelLesson(id,dateAdded,lessonName,lecturerName,defaultColor,defaultAbsenteeism,belowProgram)
                lessonID = createdLesson.id

                viewModel.storeLessonInDatabase(createdLesson)


                binding.fragmentDetailsImageViewDelete.visibility = View.VISIBLE
                binding.fragmentDetailsImageViewAddNewLesson.visibility = View.GONE
                binding.fragmentDetailsImageVieCancelAddNewLesson.visibility = View.GONE
                binding.fragmentDetailsTextViewAddProgram.visibility = View.VISIBLE

            }else{
                binding.fragmentDetailsTextFieldLessonName.isErrorEnabled = true
                binding.fragmentDetailsTextFieldLessonName.error = "ders adi bos birakilamaz"
            }


        }


        binding.fragmentDetailsImageViewDelete.setOnClickListener {

            if (isNewLesson){
                viewModel.deleteLesson(lessonID)
                val actionToMain = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
                findNavController().navigate(actionToMain)
            }else{
                val title = resources.getString(R.string.alertDialog_titleDelete)
                val message = resources.getString(R.string.alertDialog_sureDelete)

                showAlert(title,message){
                    if(it){
                        viewModel.deleteLesson(lessonID)
                        Toast.makeText(context,resources.getString(R.string.toast_successful),Toast.LENGTH_LONG).show()
                        val actionToMain = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
                        findNavController().navigate(actionToMain)
                    }
                }
            }
        }

        observeLiveData()
    }


//    fun createNewLesson(){
//
//        //bu fonksiyonun amaci sudur:
//        //kullanici ana sayfadan buraya yeni bir ders eklemek icin geliyorsa bu fragment acildiginda direkt yeni bir ders olusturulur
//        //ancak bu olusturulan dersin nullable fieldlari database e sonradan degistirilmek uzere null olarak kaydedilir
//        //kullanici bu fragmentte her ekledigi deger icin databade guncellenir
//
//        val programName = belowProgramID.split("_")[0]
//        val id = IDGenerator().generateLessonID(programName, "jghfjf")
//        val dateAdded = SimpleDateFormat("dd.MM.yyyy-HH:mm:ss").format(Calendar.getInstance().time)
//        val defaultColor = "#C62910"
//        val defaultAbsenteeism = "0"
//        val belowProgram = belowProgramID
//
//        createdLesson = ModelLesson(id,dateAdded,null,null,defaultColor,defaultAbsenteeism,belowProgram)
//        lessonID = createdLesson.id
//
//        viewModel.storeLessonInDatabase(createdLesson)
//    }


    fun observeLiveData(){
        viewModel.lessonName.observe(viewLifecycleOwner, Observer {
            binding.fragmentDetailsEditTextLessonName.setText(it)
        })
        viewModel.lecturerName.observe(viewLifecycleOwner, Observer {
            binding.fragmentDetailsEditTextLecturerName.setText(it)
        })
        viewModel.programTimes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                isEmpty = false
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

    fun showChangeValueAlert(title: String, message: String, currentString: String, response: (String) -> Unit){

        //bu fonksiyonun olayi kullanici edit texte bastiginda acilir ve kullanicinin yeni veri girmesini ister
        //dogrudan edit textten duzenlemek yerine alert dialog acilir ve oradan duzenleme istenir.
        //neden boyle ugrastiriyorsun diye sorma cok zorladi bu kaydetme olayi o yuzden en iyi yol olarak simdilik bu.
        //cok kurcalamadan yoluna bak

        val alertDialogBuilder = AlertDialog.Builder(activity)

        val string = requireActivity().resources
        val inflater = requireActivity().layoutInflater

        //custom viewi getir
        val view = inflater.inflate(R.layout.edit_text_dialog_layout, null)


        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setTitle(title)
        //custom viewi set et
        alertDialogBuilder.setView(view)

        //custom viewdeki gorunumleri tanimla
        val editText: EditText =  view.findViewById(R.id.alertDialogEditText)
        val textView: TextView = view.findViewById(R.id.alertDialogTextViewcurrentString)

        textView.setText(currentString)
        alertDialogBuilder.setPositiveButton(string.getString(R.string.alertDialog_saveButton)) { dialog, id ->
            //eger kullanici kaydete basarsa edit texte yazdigi stringle birlikte true gonder
            response(editText.text.toString().trimEnd())
        }
        alertDialogBuilder.setNegativeButton(string.getString(R.string.alertDialog_cancelButton)){ dialog, id ->
            response("null")
        }

        alertDialogBuilder.show()
    }

    fun onBackPressed(){
        //geri donme butonu

        if(isEmpty){
            //eger kullanici ders icerisine henuz bir program(time) eklememisse bu ders silinir

            //ders adi veya ogretmen adi texti bos degil ise aler dialog goster
            if (binding.fragmentDetailsEditTextLessonName.text.toString() != "" || binding.fragmentDetailsEditTextLecturerName.text.toString() != ""){
                showAlert("Alert","eger program eklemezseniz ders silinecektir. devam etmek istiyormusunuz"){
                    if (it){
                        viewModel.deleteLesson(createdLesson.id)
                        val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
                        findNavController().navigate(actionToBack)
                    }
                }
            }else{
                viewModel.deleteLesson(createdLesson.id)
                val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
                findNavController().navigate(actionToBack)
            }


        }else{
            val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
            findNavController().navigate(actionToBack)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }




}