# PicoBlazeRules

Convert Snort rules to PicoBlaze instructions using an Aho-Corasick tree.

## Usage

    ./picoblazerules rules_file

## Rules file example

    alert any 192.168.1.0/16 160:80 -> 80.21.1.0/24 4242:8080 (content:"his"; msg: "mountd access";)
    alert udp 12.2.3.4 12:37 -> 127.0.0.1 111:1256 (content:"she"; msg: "mountd access";)
    alert tcp any any -> 192.168.1.0/24 21 (content: "her"; msg: "FTP root login";)
    
## Output example

    // Idle state
    /* 0x0 */ 0x00, 0x00, 0x00, 0x00
    /* 0x4 */ 0x01, 0x68, 0x00, 0x10
    /* 0x8 */ 0x01, 0x73, 0x00, 0x80
    /* 0xc */ 0x02, 0x00, 0x00, 0x00
    
    // state "h"
    /* 0x10 */ 0x00, 0x00, 0x00, 0x00
    /* 0x14 */ 0x01, 0x65, 0x00, 0x20
    /* 0x18 */ 0x01, 0x69, 0x00, 0x50
    /* 0x1c */ 0x03, 0x00, 0x00, 0x00
    
    // state "he"
    /* 0x20 */ 0x00, 0x00, 0x00, 0x00
    /* 0x24 */ 0x01, 0x72, 0x00, 0x2c
    /* 0x28 */ 0x03, 0x00, 0x00, 0x00
    
    // state "her"
    /* 0x2c */ 0x00, 0x00, 0x00, 0x01
    /* 0x30 */ 0x03, 0x00, 0x00, 0x00
    /* 0x34 */ 0x04, 0x00, 0x00, 0x06
    /* 0x38 */ 0x00, 0x00, 0x00, 0x00
    /* 0x3c */ 0xff, 0xff, 0xff, 0xff
    /* 0x40 */ 0x00, 0x00, 0xff, 0xff
    /* 0x44 */ 0xc0, 0xa8, 0x01, 0x00
    /* 0x48 */ 0xc0, 0xa8, 0x01, 0xff
    /* 0x4c */ 0x00, 0x15, 0x00, 0x15
    
    // state "hi"
    /* 0x50 */ 0x00, 0x00, 0x00, 0x00
    /* 0x54 */ 0x01, 0x73, 0x00, 0x5c
    /* 0x58 */ 0x03, 0x00, 0x00, 0x00
    
    // state "his"
    /* 0x5c */ 0x00, 0x00, 0x00, 0x02
    /* 0x60 */ 0x03, 0x00, 0x00, 0x80
    /* 0x64 */ 0x04, 0x00, 0x00, 0xff
    /* 0x68 */ 0xc0, 0xa8, 0x00, 0x00
    /* 0x6c */ 0xc0, 0xa8, 0xff, 0xff
    /* 0x70 */ 0x00, 0x50, 0x00, 0xa0
    /* 0x74 */ 0x50, 0x15, 0x01, 0x00
    /* 0x78 */ 0x50, 0x15, 0x01, 0xff
    /* 0x7c */ 0x10, 0x92, 0x1f, 0x90
    
    // state "s"
    /* 0x80 */ 0x00, 0x00, 0x00, 0x00
    /* 0x84 */ 0x01, 0x68, 0x00, 0x8c
    /* 0x88 */ 0x03, 0x00, 0x00, 0x00
    
    // state "sh"
    /* 0x8c */ 0x00, 0x00, 0x00, 0x00
    /* 0x90 */ 0x01, 0x65, 0x00, 0x98
    /* 0x94 */ 0x03, 0x00, 0x00, 0x10
    
    // state "she"
    /* 0x98 */ 0x00, 0x00, 0x00, 0x03
    /* 0x9c */ 0x03, 0x00, 0x00, 0x20
    /* 0xa0 */ 0x04, 0x00, 0x00, 0x11
    /* 0xa4 */ 0x0c, 0x02, 0x03, 0x04
    /* 0xa8 */ 0x0c, 0x02, 0x03, 0x04
    /* 0xac */ 0x00, 0x0c, 0x00, 0x25
    /* 0xb0 */ 0x7f, 0x00, 0x00, 0x01
    /* 0xb4 */ 0x7f, 0x00, 0x00, 0x01
    /* 0xb8 */ 0x00, 0x6f, 0x04, 0xe8
    
    
    INIT_00 => X"0000000350006901200065010000000000000002800073011000680100000000",
    INIT_01 => X"FFFFFFFF00000000060000040000000301000000000000032C00720100000000",
    INIT_02 => X"02000000000000035C0073010000000015001500FF01A8C00001A8C0FFFF0000",
    INIT_03 => X"901F9210FF01155000011550A0005000FFFFA8C00000A8C0FF00000480000003",
    INIT_04 => X"2000000303000000100000039800650100000000000000038C00680100000000",
    INIT_05 => X"00000000E8046F000100007F0100007F25000C000403020C0403020C11000004"
    
## Contact
    
    David Carnot
    dc386@kent.ac.uk

    Jean-Charles Le Goff
    jcl28@kent.ac.uk

    Valentin Briand
    vb86@kent.ac.uk

    Michael Bishaey
    mb551@kent.ac.uk