package com.example.groupproject.mapper;

import com.example.groupproject.model.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {

    void addAddress(Address address);

    List<Address> selectAddressByUserId(Long userId);

    void updateAddress(Address address);

    Integer countAddress();

    void deleteAddress(Long id);

    Address selectAddressById(Long id);

    List<Address> selectAddressByPhoneNumber(String phoneNumber);
}
