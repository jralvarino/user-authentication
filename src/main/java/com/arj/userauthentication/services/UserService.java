package com.arj.userauthentication.services;

import com.arj.userauthentication.dtos.PageResponse;
import com.arj.userauthentication.dtos.UserDTO;
import com.arj.userauthentication.entities.UserEntity;
import com.arj.userauthentication.enums.ProfileTypeEnum;
import com.arj.userauthentication.enums.SequencesEnum;
import com.arj.userauthentication.exceptions.NotFoundException;
import com.arj.userauthentication.exceptions.SequenceException;
import com.arj.userauthentication.repositories.CustomUserInterface;
import com.arj.userauthentication.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService  {

  private UserRepository userRepository;
  private ModelMapper modelMapper;
  private SequenceGeneratorService sequenceGeneratorService;
  private BCryptPasswordEncoder passwordEncoder;
  private CustomUserInterface customUserInterface;

  public UserService(UserRepository userRepository, ModelMapper modelMapper, SequenceGeneratorService sequenceGeneratorService, BCryptPasswordEncoder passwordEncoder, CustomUserInterface customUserInterface){
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
    this.sequenceGeneratorService = sequenceGeneratorService;
    this.passwordEncoder = passwordEncoder;
    this.customUserInterface = customUserInterface;
  }

  public UserDTO createUser(UserDTO userDTO) throws SequenceException {
    UserEntity userEntity = convertDtoToEntity(userDTO);
    userEntity.setId(sequenceGeneratorService.nextSequence(SequencesEnum.SEQUENCE_USERS));
    userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

    UserEntity user = userRepository.save(userEntity);

    return convertEntityToDto(user);
  }

  public PageResponse retrieveUsersWithPagination(int page, int size, String sort, String name, String email, String profile) {
    Page<UserEntity> pageUsers = customUserInterface.findAllWithPagination(page, size, sort, name, email, ProfileTypeEnum.valueOf(profile));

    return new PageResponse(pageUsers.getContent(), pageUsers.getNumber(), pageUsers.getTotalElements(), pageUsers.getTotalPages());
  }

  public void deleteUser(Long userId) throws NotFoundException {
    UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

    userRepository.delete(user);
  }

  public UserDTO updateUser(Long userId, UserDTO userDTO) throws NotFoundException {
    userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

    UserEntity userEntity = convertDtoToEntity(userDTO);
    userEntity.setId(userId);
    userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

    UserEntity user = userRepository.save(userEntity);

    return convertEntityToDto(user);
  }

  private UserEntity convertDtoToEntity(UserDTO userDTO) {
    modelMapper.getConfiguration().setAmbiguityIgnored(true);

    return modelMapper.map(userDTO, UserEntity.class);
  }

  private UserDTO convertEntityToDto(UserEntity userEntity) {
    modelMapper.getConfiguration().setAmbiguityIgnored(true);

    return modelMapper.map(userEntity, UserDTO.class);
  }



}
