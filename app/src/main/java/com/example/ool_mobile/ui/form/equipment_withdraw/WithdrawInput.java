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

        Observable.range(0, fields.getEmployees().size())
                .filter(index ->
                        fields.getEmployees().get(index).cpf().equals(withdraw.getEmployeeId()))
                .firstElement()
                .blockingSubscribe(employeeSelection::set);

        Observable.range(0, fields.getEquipments().size())
                .filter(index ->
                        fields.getEquipments().get(index).getId() == withdraw.getEquipmentId())
                .firstElement()
                .blockingSubscribe(equipmentSelection::set);

        Observable.range(0, fields.getPhotoshoots().size())
                .filter(index -> fields.getPhotoshoots()
                        .get(index)
                        .resourceId()
                        .equals(withdraw.getPhotoshootId())
                ).firstElement()
                .blockingSubscribe(photoshootSelection::set);
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
