import React, { useState } from 'react';
import {
    Box,
    Tabs,
    Tab,
    Typography,
    Button,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    TextField,
    Stack,
    IconButton,
    Paper,
    TableContainer,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit'
import { exportReport } from '../api/services/reportService';

interface RoomType {
    id: number;
    description: string;
}

interface Room {
    id: number;
    name: string;
    type: string;
    status: string;
    floor: number;
    price: number;
    description: string;
}

interface Payment {
    id: number;
    bookingId: number;
    date: string;
    amount: number;
    paymentTypeId: number;
    paymentStatusId: number;
}

interface PaymentType {
    id: number;
    description: string;
}

interface Booking {
    id: number;
    guestId: number;
    reservationAgentId: number;
    dateFrom: string;
    dateTo: string;
    bookingStatusId: number;
}


interface BookingStatus {
    id: number;
    status: string;
    description: string;
    active: number;
}

function a11yProps(index: number) {
    return {
        id: `category-tab-${index}`,
        'aria-controls': `category-tabpanel-${index}`,
    };
}

function TabPanel(props: { children?: React.ReactNode; index: number; value: number }) {
    const { children, value, index, ...other } = props;
    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`category-tabpanel-${index}`}
            aria-labelledby={`category-tab-${index}`}
            {...other}
        >
            {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
        </div>
    );
}

const dummyRoomTypes: RoomType[] = [
    { id: 1, description: 'Single' },
    { id: 2, description: 'Double' },
    { id: 3, description: 'Suite' },
];

const dummyRooms: Room[] = [
    {
        id: 1,
        name: 'Room 101',
        type: 'Single',
        status: 'Available',
        floor: 1,
        price: 100,
        description: 'Cozy single room',
    },
    {
        id: 2,
        name: 'Room 102',
        type: 'Double',
        status: 'Occupied',
        floor: 1,
        price: 150,
        description: 'Spacious double room',
    },
];

const dummyPayments: Payment[] = [
    {
        id: 1,
        bookingId: 101,
        date: '2025-06-10',
        amount: 200,
        paymentTypeId: 1,
        paymentStatusId: 1,
    },
];

const dummyPaymentTypes: PaymentType[] = [
    { id: 1, description: 'Credit Card' },
    { id: 2, description: 'Cash' },
];

const dummyBookingStatuses: BookingStatus[] = [
    {
        id: 1,
        status: "Confirmed",
        description: "Booking has been confirmed.",
        active: 1
    },
    {
        id: 2,
        status: "Pending",
        description: "Booking is pending confirmation.",
        active: 1
    },
    {
        id: 3,
        status: "Cancelled",
        description: "Booking has been cancelled.",
        active: 0
    }
];

const dummyBookings: Booking[] = [
    {
        id: 1,
        guestId: 101,
        reservationAgentId: 201,
        dateFrom: "2024-05-01",
        dateTo: "2024-05-07",
        bookingStatusId: 1
    },
    {
        id: 2,
        guestId: 102,
        reservationAgentId: 202,
        dateFrom: "2024-06-15",
        dateTo: "2024-06-20",
        bookingStatusId: 2
    },
    {
        id: 3,
        guestId: 103,
        reservationAgentId: 203,
        dateFrom: "2024-07-10",
        dateTo: "2024-07-15",
        bookingStatusId: 3
    },
    {
        id: 4,
        guestId: 104,
        reservationAgentId: 204,
        dateFrom: "2024-08-05",
        dateTo: "2024-08-10",
        bookingStatusId: 1
    }
];

function Data() {
    const [tabIndex, setTabIndex] = useState(0);

    const [roomTypes, setRoomTypes] = useState(dummyRoomTypes);
    const [rooms, setRooms] = useState(dummyRooms);
    const [payments, setPayments] = useState(dummyPayments);
    const [paymentTypes, setPaymentTypes] = useState(dummyPaymentTypes);
    const [bookings, setBookings] = useState(dummyBookings);
    const [bookingStatuses, setBookingStatuses] = useState(dummyBookingStatuses);

    const [isAdding, setIsAdding] = useState(false);
    const [editingId, setEditingId] = useState<number | null>(null);

    const [formData, setFormData] = useState<any>({});

    const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
        setTabIndex(newValue);
        setIsAdding(false);
        setEditingId(null);
        setFormData({});
    };

    const handleInputChange = (field: string, value: any) => {
        setFormData((prev: any) => ({ ...prev, [field]: value }));
    };

    const handleSave = () => {
        switch (tabIndex) {
            case 0: // Room Types
                if (editingId !== null) {
                    setRoomTypes((prev) =>
                        prev.map((rt) => (rt.id === editingId ? { ...rt, ...formData } : rt))
                    );
                } else {
                    setRoomTypes((prev) => [...prev, { id: Date.now(), ...formData }]);
                }
                break;

            case 1: // Rooms
                if (editingId !== null) {
                    setRooms((prev) => prev.map((r) => (r.id === editingId ? { ...r, ...formData } : r)));
                } else {
                    setRooms((prev) => [...prev, { id: Date.now(), ...formData }]);
                }
                break;

            case 2: // Payments
                if (editingId !== null) {
                    setPayments((prev) =>
                        prev.map((p) => (p.id === editingId ? { ...p, ...formData } : p))
                    );
                } else {
                    setPayments((prev) => [...prev, { id: Date.now(), ...formData }]);
                }
                break;

            case 3: // Payment Types
                if (editingId !== null) {
                    setPaymentTypes((prev) =>
                        prev.map((pt) => (pt.id === editingId ? { ...pt, ...formData } : pt))
                    );
                } else {
                    setPaymentTypes((prev) => [...prev, { id: Date.now(), ...formData }]);
                }
                break;

            case 4: // Bookings
                if (editingId !== null) {
                    setBookings((prev) =>
                        prev.map((s) => (s.id === editingId ? { ...s, ...formData } : s))
                    );
                } else {
                    setBookings((prev) => [...prev, { id: Date.now(), ...formData }]);
                }
                break;

            case 5: // Booking Statuses
                if (editingId !== null) {
                    setBookingStatuses((prev) =>
                        prev.map((st) => (st.id === editingId ? { ...st, ...formData } : st))
                    );
                } else {
                    setBookingStatuses((prev) => [...prev, { id: Date.now(), ...formData }]);
                }
                break;
        }
        setIsAdding(false);
        setEditingId(null);
        setFormData({});
    };

    const handleEdit = (id: number) => {
        let item;
        switch (tabIndex) {
            case 0:
                item = roomTypes.find((rt) => rt.id === id);
                break;
            case 1:
                item = rooms.find((r) => r.id === id);
                break;
            case 2:
                item = payments.find((p) => p.id === id);
                break;
            case 3:
                item = paymentTypes.find((pt) => pt.id === id);
                break;
            case 4:
                item = bookings.find((pt) => pt.id === id);
                break;
            case 5:
                item = bookingStatuses.find((pt) => pt.id === id);
                break;
        }
        if (item) {
            setFormData(item);
            setEditingId(id);
            setIsAdding(true);
        }
    };

    const renderTableRows = () => {
        switch (tabIndex) {
            case 0:
                return roomTypes.map(({ id, description }) => (
                    <TableRow key={id}>
                        <TableCell>{id}</TableCell>
                        <TableCell>{description}</TableCell>
                        <TableCell align="right">
                            <IconButton onClick={() => handleEdit(id)}><EditIcon /></IconButton>
                        </TableCell>
                    </TableRow>
                ));
            case 1:
                return rooms.map(({ id, name, type, status, floor, price, description }) => (
                    <TableRow key={id}>
                        <TableCell>{id}</TableCell>
                        <TableCell>{name}</TableCell>
                        <TableCell>{type}</TableCell>
                        <TableCell>{status}</TableCell>
                        <TableCell>{floor}</TableCell>
                        <TableCell>{price}</TableCell>
                        <TableCell>{description}</TableCell>
                        <TableCell align="right">
                            <IconButton onClick={() => handleEdit(id)}><EditIcon /></IconButton>
                        </TableCell>
                    </TableRow>
                ));
            case 2:
                return payments.map(({ id, bookingId, date, amount, paymentTypeId, paymentStatusId }) => (
                    <TableRow key={id}>
                        <TableCell>{id}</TableCell>
                        <TableCell>{bookingId}</TableCell>
                        <TableCell>{date}</TableCell>
                        <TableCell>{amount}</TableCell>
                        <TableCell>{paymentTypeId}</TableCell>
                        <TableCell>{paymentStatusId}</TableCell>
                        <TableCell align="right">
                            <IconButton onClick={() => handleEdit(id)}><EditIcon /></IconButton>
                        </TableCell>
                    </TableRow>
                ));
            case 3:
                return paymentTypes.map(({ id, description }) => (
                    <TableRow key={id}>
                        <TableCell>{id}</TableCell>
                        <TableCell>{description}</TableCell>
                        <TableCell align="right">
                            <IconButton onClick={() => handleEdit(id)}><EditIcon /></IconButton>
                        </TableCell>
                    </TableRow>
                ));

            case 4:
                return bookings.map(({ id, dateFrom, dateTo, bookingStatusId }) => (
                    <TableRow key={id}>
                        <TableCell>{id}</TableCell>
                        <TableCell>{dateFrom}</TableCell>
                        <TableCell>{dateTo}</TableCell>
                        <TableCell>{bookingStatusId}</TableCell>
                        <TableCell align="right">
                            <IconButton onClick={() => handleEdit(id)}><EditIcon /></IconButton>
                        </TableCell>
                    </TableRow>
                ));

            case 5:
                return bookingStatuses.map(({ id, status, description, active }) => (
                    <TableRow key={id}>
                        <TableCell>{id}</TableCell>
                        <TableCell>{status}</TableCell>
                        <TableCell>{description}</TableCell>
                        <TableCell>{active}</TableCell>
                        <TableCell align="right">
                            <IconButton onClick={() => handleEdit(id)}><EditIcon /></IconButton>
                        </TableCell>
                    </TableRow>
                ));
        }
    };

    const renderTableHeaders = () => {
        switch (tabIndex) {
            case 0:
                return (
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Description</TableCell>
                        <TableCell align="right">Actions</TableCell>
                    </TableRow>
                );
            case 1:
                return (
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Name</TableCell>
                        <TableCell>Type</TableCell>
                        <TableCell>Status</TableCell>
                        <TableCell>Floor</TableCell>
                        <TableCell>Price</TableCell>
                        <TableCell>Description</TableCell>
                        <TableCell align="right">Actions</TableCell>
                    </TableRow>
                );
            case 2:
                return (
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Booking ID</TableCell>
                        <TableCell>Date</TableCell>
                        <TableCell>Amount</TableCell>
                        <TableCell>Payment Type ID</TableCell>
                        <TableCell>Payment Status ID</TableCell>
                        <TableCell align="right">Actions</TableCell>
                    </TableRow>
                );
            case 3:
                return (
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Description</TableCell>
                        <TableCell align="right">Actions</TableCell>
                    </TableRow>
                );
            case 4:
                return (
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Date From</TableCell>
                        <TableCell>Date To</TableCell>
                        <TableCell>Booking Status</TableCell>
                        <TableCell align="right">Actions</TableCell>
                    </TableRow>
                );

            case 5:
                return (
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Status</TableCell>
                        <TableCell>Description</TableCell>
                        <TableCell>Active</TableCell>
                    </TableRow>
                );
        }
    };

    const renderForm = () => {
        switch (tabIndex) {
            case 0:
                return (
                    <>
                        <TextField
                            label="Description"
                            fullWidth
                            margin="normal"
                            value={formData.description || ''}
                            onChange={(e) => handleInputChange('description', e.target.value)}
                        />
                    </>
                );
            case 1:
                return (
                    <>
                        <TextField
                            label="Name"
                            fullWidth
                            margin="normal"
                            value={formData.name || ''}
                            onChange={(e) => handleInputChange('name', e.target.value)}
                        />
                        <TextField
                            label="Type"
                            fullWidth
                            margin="normal"
                            value={formData.type || ''}
                            onChange={(e) => handleInputChange('type', e.target.value)}
                        />
                        <TextField
                            label="Status"
                            fullWidth
                            margin="normal"
                            value={formData.status || ''}
                            onChange={(e) => handleInputChange('status', e.target.value)}
                        />
                        <TextField
                            label="Floor"
                            type="number"
                            fullWidth
                            margin="normal"
                            value={formData.floor || ''}
                            onChange={(e) => handleInputChange('floor', Number(e.target.value))}
                        />
                        <TextField
                            label="Price"
                            type="number"
                            fullWidth
                            margin="normal"
                            value={formData.price || ''}
                            onChange={(e) => handleInputChange('price', Number(e.target.value))}
                        />
                        <TextField
                            label="Description"
                            multiline
                            rows={3}
                            fullWidth
                            margin="normal"
                            value={formData.description || ''}
                            onChange={(e) => handleInputChange('description', e.target.value)}
                        />
                    </>
                );
            case 2:
                return (
                    <>
                        <TextField
                            label="Booking ID"
                            type="number"
                            fullWidth
                            margin="normal"
                            value={formData.bookingId || ''}
                            onChange={(e) => handleInputChange('bookingId', Number(e.target.value))}
                        />
                        <TextField
                            label="Date"
                            type="date"
                            fullWidth
                            margin="normal"
                            InputLabelProps={{ shrink: true }}
                            value={formData.date || ''}
                            onChange={(e) => handleInputChange('date', e.target.value)}
                        />
                        <TextField
                            label="Amount"
                            type="number"
                            fullWidth
                            margin="normal"
                            value={formData.amount || ''}
                            onChange={(e) => handleInputChange('amount', Number(e.target.value))}
                        />
                        <TextField
                            label="Payment Type ID"
                            type="number"
                            fullWidth
                            margin="normal"
                            value={formData.paymentTypeId || ''}
                            onChange={(e) => handleInputChange('paymentTypeId', Number(e.target.value))}
                        />
                        <TextField
                            label="Payment Status ID"
                            type="number"
                            fullWidth
                            margin="normal"
                            value={formData.paymentStatusId || ''}
                            onChange={(e) => handleInputChange('paymentStatusId', Number(e.target.value))}
                        />
                    </>
                );
            case 3:
                return (
                    <>
                        <TextField
                            label="Description"
                            fullWidth
                            margin="normal"
                            value={formData.description || ''}
                            onChange={(e) => handleInputChange('description', e.target.value)}
                        />
                    </>
                );

            case 4:
                return (
                    <>
                        <TextField
                            label="Date From"
                            fullWidth
                            margin="normal"
                            value={formData.dateFrom || ''}
                            onChange={(e) => handleInputChange('dateFrom', e.target.value)}
                        />
                        <TextField
                            label="Date To"
                            fullWidth
                            margin="normal"
                            value={formData.dateTo || ''}
                            onChange={(e) => handleInputChange('dateTo', e.target.value)}
                        />
                        <TextField
                            label="Booking Status"
                            fullWidth
                            margin="normal"
                            value={formData.BookingStatus || ''}
                            onChange={(e) => handleInputChange('bookingStatus', e.target.value)}
                        />
                    </>
                );

            case 5:
                return (
                    <>
                        <TextField
                            label="Status"
                            fullWidth
                            margin="normal"
                            value={formData.status || ''}
                            onChange={(e) => handleInputChange('status', e.target.value)}
                        />

                        <TextField
                            label="Description"
                            fullWidth
                            margin="normal"
                            value={formData.description || ''}
                            onChange={(e) => handleInputChange('description', e.target.value)}
                        />

                        <TextField
                            label="Active"
                            fullWidth
                            margin="normal"
                            value={formData.active || ''}
                            onChange={(e) => handleInputChange('active', e.target.value)}
                        />
                    </>
                );
        }
    };

    return (
        <Box sx={{ width: '100%', typography: 'body1' }}>
            <Tabs
                value={tabIndex}
                onChange={handleTabChange}
                aria-label="Categories tabs"
                variant="scrollable"
                scrollButtons="auto"
                sx={{ mb: 2 }}
            >
                <Tab label="Room Types" {...a11yProps(0)} />
                <Tab label="Rooms" {...a11yProps(1)} />
                <Tab label="Payments" {...a11yProps(2)} />
                <Tab label="Payment Types" {...a11yProps(3)} />
                <Tab label="Bookings" {...a11yProps(4)} />
                <Tab label="Booking Statuses" {...a11yProps(5)} />
            </Tabs>

            {[0, 1, 2, 3, 4, 5].map((index) => (
                <TabPanel key={index} value={tabIndex} index={index}>
                    <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
                        <Typography variant="h6">
                            {['Room Types', 'Rooms', 'Payments', 'Payment Types', 'Bookings', 'Booking Statuses'][index]}
                        </Typography>
                        {!isAdding && (
                            <Box sx={{
                                display: 'flex',
                                gap: '8px'
                            }}>
                                {tabIndex === 4 && <Button variant="contained" onClick={() => {
                                    exportReport().then((response) => {
                                        window.open(URL.createObjectURL(response.data));
                                    })
                                        ;
                                }}>
                                    Export Report
                                </Button>}
                                <Button variant="contained" onClick={() => {
                                    setIsAdding(true);
                                    setEditingId(null);
                                    setFormData({});
                                }}>
                                    Add New
                                </Button>

                            </Box>


                        )}
                    </Stack>

                    {isAdding && (
                        <Paper sx={{ p: 2, mb: 3 }}>
                            {renderForm()}
                            <Box mt={2} display="flex" gap={2}>
                                <Button variant="contained" onClick={handleSave}>
                                    {editingId !== null ? 'Update' : 'Create'}
                                </Button>
                                <Button
                                    variant="outlined"
                                    onClick={() => {
                                        setIsAdding(false);
                                        setEditingId(null);
                                        setFormData({});
                                    }}
                                >
                                    Cancel
                                </Button>
                            </Box>
                        </Paper>
                    )}
                    <TableContainer component={Paper}>
                        <Table size="small">
                            <TableHead>{renderTableHeaders()}</TableHead>
                            <TableBody>{renderTableRows()}</TableBody>
                        </Table>
                    </TableContainer>
                </TabPanel>
            ))}
        </Box>
    );
}

export default Data;
