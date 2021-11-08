package com.example.ool_mobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ool_mobile.model.Photoshoot
import com.example.ool_mobile.service.EmployeeRepository
import com.example.ool_mobile.service.api.PhotoshootApi
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel
import com.example.ool_mobile.ui.util.view_model.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.math.min

/**
 * Represents all the state and logic of HomeFragment
 *
 * This class is a great example of kotlin used with viewModels in action.
 *
 *
 * 1. Constructors
 *
 * Kotlin has a simple and nice feature that makes the need for declared constructors objectively
 * scarce. Most of time you need to declare a constructor to initialize fields, it can be done
 * through a simpler syntax.
 *
 * When you have a java class that has the following pattern:
 *
 * ```java
 *  class MyClass {
 *      private final MyDependency dependency;
 *
 *      public MyClass(MyDependency dependency) {
 *          this.dependency = dependency
 *      }
 *
 *      // ...
 *  }
 * ```
 *
 * It can be written like the following in kotlin:
 * ```kotlin
 *  class MyClass(private val myField: MyDependency) {
 *      // ...
 *  }
 * ```
 *  The parameters in the class definition will become a single constructor that does exactly what
 *  it did in the previous java example, it will initialize a field called `dependency`, which can
 *  be used later.
 *
 * 2. Initialization of ViewModel objects
 *
 * Android ViewModels are not usually obtained through their constructors by the activities.
 * We need to have a way of creating a ViewModelFactory so that a ViewModelProvider can instantiate
 * them lazily.
 *
 * This process is made easier with classes such as ViewModelFactory in java:
 *
 * ```java
 *  @NonNull
 *  public static ViewModelProvider.Factory create(Parameters parameters) {
 *      return ViewModelFactory.create(
 *          HomeViewModelTest.class,
 *          () -> new HomeViewModel(parameters)
 *      );
 *  }
 * ```
 *
 * The ViewModelFactory.create function needs the Class object to the current ViewModel
 * (so that it can check if is creating the right objects), and a lambda that
 * supplies the ViewModel object to be used, this usually calls the ViewModel constructor.
 *
 * It works fine, but kotlin has a nice feature that allows us to make such operation less error
 * prone and more readable, by using inline functions. Inline functions are functions that do not
 * get compiled normally, and their bytecode is usually inserted directly in the caller function.
 *
 * This allows the function to know a little bit more about its context, thus allowing reified
 * generic parameters, that consist of generic parameters that can give you their Class object
 * representation, something that is not normally achievable in java, and can be done in languages
 * like C# or C++. This removes the needs for providing a class parameter, and alongside
 * kotlin's particularly simple lambda syntax, the kotlin version can be more concise.
 *
 * ```kotlin
 *  fun create(Parameters parameters) = viewModelFactory {
 *      HomeViewModel(parameters)
 *  }
 * ```
 *
 * Kotlin does not have the concept of a static method like we do in java, we
 * usually put all "static" methods in a section of our class called Companion object.
 *
 * If we want the code to be nicely called from Java, we can go even further and add the @JvmStatic
 * annotation to the method, making it work just like it would with java.
 *
 * 3. Lazy fields
 *
 * We often need to fetch a value once, store its value in memory, and use this stored value
 * every time we need it. There are usually two solutions to this problem: Eager Loading and
 * Lazy Loading. Eager loading consists of fetching the resource as soon as possible, usually
 * when the ViewModel is constructed. Lazy loading consists of only fetching the value when it
 * is requested the first time, and using this value for all subsequent requests. The lazy loading
 * approach is usually the ideal as it is allows for the resources to be only used when they are
 * really needed.
 *
 * To achieve lazy loading in java, we usually perform the following idiom:
 * ```java
 * private MyValue value = null;
 *
 * public MyValue getValue() {
 *     if (value == null) {
 *         value = initializeValue();
 *     }
 *
 *     return value;
 * }
 * ```
 *
 * This idiom is expressive, but it can be quite verbose. Considering this is a common scenario,
 * kotlin usually does it through "by lazy", which has a very similar behavior.
 *
 * ```kotlin
 * val value: MyValue by lazy {
 *    initializeValue()
 * }
 * ```
 *
 * We also often need to upcast the object to one of its read only implementing interfaces,
 * to prevent the class user code from tempering with results. This is often ween with
 * Subjects x Observables in RxJava, and MutableLiveData x LiveData.
 * The java solution is maintaining the field's type as the mutable version, and making
 * the getter read only:
 *
 * ```java
 * // the field is mutable
 * private MutableLiveData<String> value = null;
 *
 * // the getter return type is read only
 * public LiveData<String> getValue() {
 *     if (value == null) {
 *         value = initializeValue();
 *     }
 *
 *     return value;
 * }
 * ```
 *
 * The same effect can be achieved in kotlin, by using two properties, one private and mutable,
 * usually starting with and underscore (_) and one public and read only, without the score:
 *
 * ```kotlin
 * private val _value: MutableLiveData<String> by lazy {
 *     initializeValue()
 * }
 *
 * val value: LiveData<String> get() = _value
 * ```
 *
 * This is usually helped by the .also function, that applies a certain function to an object,
 * and returns such object. This can allow simple read only initializations to use the previous
 * and smaller syntax.
 *
 * ```
 * val value: LiveData<String> by lazy {
 *    MutableLiveData().also {
 *        initializeValue()
 *    }
 * }
 * ```
 *
 * There is just one drawback: The initialization function *MUST* postpone the initialization
 * of the object at a later time, otherwise it will recurse and cause a stack overflow error.
 *
 */
class HomeViewModel(
        private val employeeRepository: EmployeeRepository,
        private val photoshootApi: PhotoshootApi
) : SubscriptionViewModel() {

    private val _events: Subject<Event> = PublishSubject.create()

    val events: Observable<Event> = _events

    val employeeName: LiveData<String> by lazy {

        MutableLiveData<String>().also { employeeName ->

            subscriptions.add(
                    employeeRepository.currentEmployee
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ employeeName.setValue(it.name()) })
                            { handleError(it) }
            )
        }

    }

    val dayOfWeek: LiveData<String> by lazy {
        MutableLiveData<String>().also { dayOfWeek ->
            val weekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            val currentDay = weekFormat.format(Date())
            val result = currentDay.substring(0, min(3, currentDay.length))
            dayOfWeek.value = result
        }
    }

    val dayOfMonth: LiveData<String> by lazy {
        MutableLiveData<String>().also { dayOfMonth ->
            val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
            val day = dayFormat.format(Date())
            val result = day.substring(0, min(3, day.length))
            dayOfMonth.value = result
        }
    }

    private val _pendingPhotoshoots: MutableLiveData<List<Photoshoot>> by lazy {
        MutableLiveData<List<Photoshoot>>().also {

            subscriptions.add(
                    photoshootApi.listFromCurrentEmployee()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ onPhotoshootsFetched(it) })
                            { handleError(it) }
            )
        }
    }

    val pendingPhotoshoots: LiveData<List<Photoshoot>> get() = _pendingPhotoshoots

    fun logout() {

        employeeRepository.logout()
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared<Any>())
                .subscribe({ _events.onNext(Event.Logout) })
                { handleError(it) }
    }

    interface Event {
        fun accept(visitor: Visitor)

        interface Visitor {
            fun visitError()
            fun visitLogout()
        }

        companion object {
            val Error: Event = object : Event {
                override fun accept(visitor: Visitor) {
                    visitor.visitError()
                }
            }

            val Logout: Event = object : Event {
                override fun accept(visitor: Visitor) {
                    visitor.visitLogout()
                }
            }
        }
    }


    private fun handleError(throwable: Throwable) {
        Timber.e(throwable);
        _events.onNext(Event.Error)
    }

    private fun onPhotoshootsFetched(photoshoots: List<Photoshoot>) {
        val result = photoshoots.stream()
                .filter { photoshoot: Photoshoot -> isToday(photoshoot.startTime()) }
                .collect(Collectors.toList())
        _pendingPhotoshoots.value = result
    }

    private fun isToday(date: Date): Boolean {
        return isSameDay(date, Date())
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {

        val calendar1 = Calendar.getInstance()
        calendar1.time = date1

        val calendar2 = Calendar.getInstance()
        calendar2.time = date2

        return calendar1[Calendar.DAY_OF_YEAR] == calendar2[Calendar.DAY_OF_YEAR] &&
                calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR]
    }

    companion object {
        @JvmStatic
        fun create(
                repository: EmployeeRepository,
                photoshootApi: PhotoshootApi
        ) = viewModelFactory {
            HomeViewModel(repository, photoshootApi)
        }
    }
}