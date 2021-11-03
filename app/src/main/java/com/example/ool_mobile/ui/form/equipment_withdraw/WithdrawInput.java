package com.example.ool_mobile.ui.form.equipment_withdraw;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.model.EquipmentWithdraw;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.ui.util.form.FormTime;

import org.immutables.value.Value;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Predicate;

public class WithdrawInput {

    @NonNull
    public final ObservableField<Date> expectedDevolutionDate = new ObservableField<>();

    public final ObservableField<FormTime> expectedDevolutionTime = new ObservableField<>();

    public final ObservableInt employeeSelection = new ObservableInt(-1);

    public final ObservableInt photoshootSelection = new ObservableInt(-1);

    public final ObservableInt equipmentSelection = new ObservableInt(-1);

    public WithdrawInput() {

    }

    public WithdrawInput(EquipmentWithdraw withdraw, ListFields fields) {

        expectedDevolutionDate.set(withdraw.getExpectedDevolutionDate());
        expectedDevolutionTime.set(FormTime.fromDate(withdraw.getExpectedDevolutionDate()));

        selectItem(
                employeeSelection,
                fields.getEmployees(),
                it -> it.cpf().equals(withdraw.getEmployeeId())
        );

        selectItem(
                equipmentSelection,
                fields.getEquipments(),
                it -> it.getId() == withdraw.getEquipmentId()
        );

        selectItem(
                photoshootSelection,
                fields.getPhotoshoots(),
                it -> it.resourceId().equals(withdraw.getPhotoshootId())
        );

    }

    public void selectEquipmentWithId(int id, List<Equipment> equipments) {
        selectItem(
                equipmentSelection,
                equipments,
                it -> it.getId() == id
        );
    }

    private <T> void selectItem(ObservableInt selection, List<T> items, Predicate<T> predicate) {

        Observable.range(0, items.size())
                .filter(index -> predicate.test(items.get(index))).firstElement()
                .blockingSubscribe(selection::set);

    }

    @Value.Immutable
    @Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE)
    public interface ListFields {

        @NonNull
        List<Employee> getEmployees();

        @NonNull
        List<Photoshoot> getPhotoshoots();

        @NonNull
        List<Equipment> getEquipments();
    }

}
