#!/usr/bin/env ruby

def runPacker(dir, type)
  puts dir
  puts type
  begin
    rvpacker -V -f -d #{dir} -t ace -a #{type}
  rescue Exception =>e
    puts "Error!"
    puts e.message
    puts e.backtrace.inspect 
    return 1
  end
  return 0
end

