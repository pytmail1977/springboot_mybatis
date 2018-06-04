################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../commonTool.cpp \
../global.cpp \
../myProgram.cpp 

OBJS += \
./commonTool.o \
./global.o \
./myProgram.o 

CPP_DEPS += \
./commonTool.d \
./global.d \
./myProgram.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I/usr/local/Cellar/mysql/5.7.18_1/include/mysql -I/home/pyt64/mysql-connector-c-6.1.5-src/include -O0 -g3 -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


