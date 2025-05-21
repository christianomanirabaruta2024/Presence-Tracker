package com.upg.employee_management.service;

import com.upg.employee_management.aop.Trackable;
import com.upg.employee_management.dto.HolidayDTO;
import com.upg.employee_management.model.Holiday;
import com.upg.employee_management.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Trackable
@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayDTO createHoliday(HolidayDTO holidayDTO) {
        Holiday holiday = new Holiday();
        holiday.setName(holidayDTO.getName());
        holiday.setDate(holidayDTO.getDate());
        holiday.setType(Holiday.Type.valueOf(holidayDTO.getType()));
        holiday.setCreatedAt(holidayDTO.getCreatedAt());
        holiday.setUpdatedAt(holidayDTO.getUpdatedAt());
        holiday = holidayRepository.save(holiday);
        holidayDTO.setHolidayId(holiday.getHolidayId());
        return holidayDTO;
    }

    public List<HolidayDTO> getAllHolidays() {
        return holidayRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<HolidayDTO> getHolidayById(Long id) {
        return holidayRepository.findById(id).map(this::convertToDTO);
    }

    private HolidayDTO convertToDTO(Holiday holiday) {
        HolidayDTO dto = new HolidayDTO();
        dto.setHolidayId(holiday.getHolidayId());
        dto.setName(holiday.getName());
        dto.setDate(holiday.getDate());
        dto.setType(holiday.getType().name());
        dto.setCreatedAt(holiday.getCreatedAt());
        dto.setUpdatedAt(holiday.getUpdatedAt());
        return dto;
    }
}