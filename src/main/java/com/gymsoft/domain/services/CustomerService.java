package com.gymsoft.domain.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gymsoft.domain.dto.CustomerDTO;
import com.gymsoft.domain.user.entity.Customer;
import com.gymsoft.domain.user.repository.CustomerRepository;

@Service("customerService")
public class CustomerService{

	private CustomerRepository customerRepository;
	
	CustomerService(CustomerRepository customerRepository){
		this.customerRepository = customerRepository;
	}
	
	public Customer addCustomer(CustomerDTO customerDto) {
		
		Objects.requireNonNull(customerDto);
		
		String customerEmail = customerDto.getEmail();
		
		Optional<Customer> customerFound = customerRepository.findByEmail(customerEmail);
		
		if(customerFound.isPresent()) {
			
			throw new RuntimeException("Customer already exist with this email");
		}
		
		Customer customer = new Customer();
		customer.setName(customerDto.getName());				
		customer.setEmail(customerDto.getEmail());	
		customer.setMobile(customerDto.getMobile());
		customer.setDateOfJoin(customerDto.getDateOfJoin());
		customer.setLastDate(customerDto.getLastDate());
		customer.setGender(customerDto.getGender());
		customer.setPin(customerDto.getPin());		
		customer.setKycType(customerDto.getKycType());
		customer.setDocNumber(customerDto.getDocNumber());
		String avatarName = customerDto.getName() +"_"+ customerDto.getPin();
		String docImageName = customerDto.getKycType()+"_"+ customerDto.getName() +"_"+ customerDto.getPin();
		
		if(storeImage(customerDto.getAvatar(), avatarName)) {
			customer.setAvatar(avatarName);
		}
		if(storeImage(customerDto.getDocImage(), docImageName)) {
			customer.setDocImage(docImageName);
		};
		
		customerRepository.save(customer);
		
		return customer;
		
	}

	private boolean storeImage(MultipartFile imageFile, String name) {
		try {
			if(imageFile != null) {
				byte[] fileBytes = imageFile.getBytes();
				Path path = Paths.get("/photos/" + name +".jpg" );
	            Files.write(path, fileBytes);
	            return true;
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Optional<Customer> getCustomer(String email)
	{		
		return customerRepository.findByEmail(email);
	}

	public List<Customer> getAllCustomers()
	{		
		return customerRepository.findAll();
	}

}
