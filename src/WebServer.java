import io.javalin.Javalin;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class WebServer {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ArrayList<Menu> daftarMenu = new ArrayList<>();
    
    // Konstanta untuk biaya dan diskon
    private static final int BIAYA_PELAYANAN = 20000;
    private static final int MINIMUM_B1G1_AMOUNT = 50000;
    private static final int MINIMUM_DISCOUNT_AMOUNT = 100000;
    
    public static void inisialisasiMenu() {
        if (daftarMenu.isEmpty()) {
            // Kategori Makanan (minimal 4)
            daftarMenu.add(new Menu("Nasi Goreng", 25000, "Makanan"));
            daftarMenu.add(new Menu("Mie Goreng", 22000, "Makanan"));
            daftarMenu.add(new Menu("Ayam Bakar", 35000, "Makanan"));
            daftarMenu.add(new Menu("Sate Ayam", 30000, "Makanan"));

            // Kategori Minuman (minimal 4)
            daftarMenu.add(new Menu("Es Teh Manis", 8000, "Minuman"));
            daftarMenu.add(new Menu("Es Jeruk", 10000, "Minuman"));
            daftarMenu.add(new Menu("Kopi Hitam", 12000, "Minuman"));
            daftarMenu.add(new Menu("Jus Alpukat", 15000, "Minuman"));
        }
    }
    
    public static void start(int port) {
        inisialisasiMenu();
        
        Javalin app = Javalin.create().start(port);
        
        System.out.println("Web server started at http://localhost:" + port);
        System.out.println("Press Ctrl+C to stop the server");
        
        // API Endpoints
        
        // Get all menu items
        app.get("/api/menu", ctx -> {
            ctx.json(daftarMenu);
        });
        
        // Get menu grouped by category
        app.get("/api/menu/grouped", ctx -> {
            Map<String, List<Menu>> grouped = new HashMap<>();
            grouped.put("Makanan", new ArrayList<>());
            grouped.put("Minuman", new ArrayList<>());
            
            for (Menu menu : daftarMenu) {
                grouped.get(menu.getKategori()).add(menu);
            }
            ctx.json(grouped);
        });
        
        // Add new menu item
        app.post("/api/menu", ctx -> {
            Menu newMenu = objectMapper.readValue(ctx.body(), Menu.class);
            daftarMenu.add(newMenu);
            ctx.status(201).json(newMenu);
        });
        
        // Update menu price
        app.put("/api/menu/{index}", ctx -> {
            int index = Integer.parseInt(ctx.pathParam("index"));
            if (index >= 0 && index < daftarMenu.size()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> body = objectMapper.readValue(ctx.body(), Map.class);
                int newPrice = ((Number) body.get("harga")).intValue();
                daftarMenu.get(index).setHarga(newPrice);
                ctx.json(daftarMenu.get(index));
            } else {
                ctx.status(404).result("Menu not found");
            }
        });
        
        // Delete menu item
        app.delete("/api/menu/{index}", ctx -> {
            int index = Integer.parseInt(ctx.pathParam("index"));
            if (index >= 0 && index < daftarMenu.size()) {
                Menu removed = daftarMenu.remove(index);
                ctx.json(removed);
            } else {
                ctx.status(404).result("Menu not found");
            }
        });
        
        // Calculate order total
        app.post("/api/order/calculate", ctx -> {
            OrderRequest orderRequest = objectMapper.readValue(ctx.body(), OrderRequest.class);
            OrderResult result = calculateOrder(orderRequest.items);
            ctx.json(result);
        });
        
        // Serve the main HTML page
        app.get("/", ctx -> {
            ctx.html(getIndexHtml());
        });
    }
    
    private static OrderResult calculateOrder(List<OrderItem> items) {
        OrderResult result = new OrderResult();
        result.items = new ArrayList<>();
        
        int subtotal = 0;
        int jumlahMinuman = 0;
        int hargaMinumanTermurah = 0;
        
        for (OrderItem item : items) {
            Menu menu = findMenuByName(item.nama);
            if (menu != null) {
                int totalItem = menu.getHarga() * item.jumlah;
                subtotal += totalItem;
                
                OrderResultItem resultItem = new OrderResultItem();
                resultItem.nama = menu.getNama();
                resultItem.harga = menu.getHarga();
                resultItem.jumlah = item.jumlah;
                resultItem.total = totalItem;
                result.items.add(resultItem);
                
                if (menu.getKategori().equals("Minuman")) {
                    jumlahMinuman += item.jumlah;
                    // Track cheapest beverage price
                    if (hargaMinumanTermurah == 0 || menu.getHarga() < hargaMinumanTermurah) {
                        hargaMinumanTermurah = menu.getHarga();
                    }
                }
            }
        }
        
        result.subtotal = subtotal;
        
        // B1G1 discount for beverages - only apply if there are beverages and subtotal threshold met
        int diskonB1G1 = 0;
        if (subtotal > MINIMUM_B1G1_AMOUNT && jumlahMinuman >= 2 && hargaMinumanTermurah > 0) {
            diskonB1G1 = hargaMinumanTermurah;
        }
        result.diskonB1G1 = diskonB1G1;
        
        int subtotalSetelahB1G1 = subtotal - diskonB1G1;
        result.subtotalSetelahB1G1 = subtotalSetelahB1G1;
        
        // Tax 10%
        int pajak = (int) (subtotalSetelahB1G1 * 0.10);
        result.pajak = pajak;
        
        // Service fee
        result.biayaPelayanan = BIAYA_PELAYANAN;
        
        int totalSementara = subtotalSetelahB1G1 + pajak + BIAYA_PELAYANAN;
        result.totalSementara = totalSementara;
        
        // 10% discount if total > 100,000
        int diskon10Persen = 0;
        if (totalSementara > MINIMUM_DISCOUNT_AMOUNT) {
            diskon10Persen = (int) (totalSementara * 0.10);
        }
        result.diskon10Persen = diskon10Persen;
        
        result.totalAkhir = totalSementara - diskon10Persen;
        
        return result;
    }
    
    private static Menu findMenuByName(String nama) {
        for (Menu menu : daftarMenu) {
            if (menu.getNama().equalsIgnoreCase(nama)) {
                return menu;
            }
        }
        return null;
    }
    
    // Inner classes for JSON serialization
    static class OrderRequest {
        List<OrderItem> items;
    }
    
    static class OrderItem {
        String nama;
        int jumlah;
    }
    
    static class OrderResult {
        List<OrderResultItem> items;
        int subtotal;
        int diskonB1G1;
        int subtotalSetelahB1G1;
        int pajak;
        int biayaPelayanan;
        int totalSementara;
        int diskon10Persen;
        int totalAkhir;
    }
    
    static class OrderResultItem {
        String nama;
        int harga;
        int jumlah;
        int total;
    }
    
    private static String getIndexHtml() {
        return """
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restoran Kasir App</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container { max-width: 1200px; margin: 0 auto; }
        h1 { 
            text-align: center; 
            color: white; 
            margin-bottom: 30px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        .tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }
        .tab-btn {
            padding: 12px 24px;
            border: none;
            background: rgba(255,255,255,0.2);
            color: white;
            cursor: pointer;
            border-radius: 8px;
            font-size: 16px;
            transition: all 0.3s;
        }
        .tab-btn:hover, .tab-btn.active {
            background: white;
            color: #667eea;
        }
        .tab-content { display: none; }
        .tab-content.active { display: block; }
        .card {
            background: white;
            border-radius: 16px;
            padding: 24px;
            margin-bottom: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        .card h2 {
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #667eea;
        }
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 16px;
        }
        .menu-item {
            border: 2px solid #e0e0e0;
            border-radius: 12px;
            padding: 16px;
            transition: all 0.3s;
            cursor: pointer;
        }
        .menu-item:hover {
            border-color: #667eea;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
        }
        .menu-item.selected {
            border-color: #667eea;
            background: #f0f4ff;
        }
        .menu-item h4 { color: #333; margin-bottom: 8px; }
        .menu-item .price { 
            color: #667eea; 
            font-weight: bold; 
            font-size: 18px; 
        }
        .menu-item .category {
            display: inline-block;
            padding: 4px 12px;
            background: #667eea;
            color: white;
            border-radius: 20px;
            font-size: 12px;
            margin-top: 8px;
        }
        .quantity-control {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-top: 12px;
        }
        .quantity-control button {
            width: 32px;
            height: 32px;
            border: none;
            background: #667eea;
            color: white;
            border-radius: 50%;
            cursor: pointer;
            font-size: 18px;
        }
        .quantity-control span {
            font-weight: bold;
            min-width: 30px;
            text-align: center;
        }
        .order-summary {
            background: #f8f9fa;
            border-radius: 12px;
            padding: 20px;
            margin-top: 20px;
        }
        .order-summary h3 {
            margin-bottom: 16px;
            color: #333;
        }
        .order-line {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #e0e0e0;
        }
        .order-line.total {
            border-top: 2px solid #333;
            border-bottom: none;
            font-weight: bold;
            font-size: 20px;
            color: #667eea;
            margin-top: 10px;
            padding-top: 15px;
        }
        .order-line.discount { color: #28a745; }
        .btn {
            padding: 14px 28px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            transition: all 0.3s;
        }
        .btn-primary {
            background: #667eea;
            color: white;
        }
        .btn-primary:hover {
            background: #5a6fd6;
        }
        .btn-danger {
            background: #dc3545;
            color: white;
        }
        .btn-danger:hover {
            background: #c82333;
        }
        .btn-success {
            background: #28a745;
            color: white;
        }
        .form-group {
            margin-bottom: 16px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 16px;
        }
        .form-group input:focus, .form-group select:focus {
            outline: none;
            border-color: #667eea;
        }
        .management-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 16px;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            margin-bottom: 12px;
        }
        .management-item .info h4 { margin-bottom: 4px; }
        .management-item .info .details { color: #666; font-size: 14px; }
        .management-item .actions { display: flex; gap: 10px; }
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }
        .modal.active { display: flex; }
        .modal-content {
            background: white;
            padding: 30px;
            border-radius: 16px;
            max-width: 400px;
            width: 90%;
        }
        .modal-content h3 { margin-bottom: 20px; }
        .receipt {
            background: #fff;
            border: 2px dashed #333;
            padding: 20px;
            font-family: 'Courier New', monospace;
            max-width: 350px;
            margin: 20px auto;
        }
        .receipt-header {
            text-align: center;
            border-bottom: 1px dashed #333;
            padding-bottom: 10px;
            margin-bottom: 10px;
        }
        .receipt-item {
            display: flex;
            justify-content: space-between;
            margin: 5px 0;
        }
        .receipt-total {
            border-top: 2px solid #333;
            margin-top: 10px;
            padding-top: 10px;
            font-weight: bold;
        }
        @media (max-width: 768px) {
            .tabs { flex-wrap: wrap; }
            .menu-grid { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🍽️ Restoran Kasir App</h1>
        
        <div class="tabs">
            <button class="tab-btn active" onclick="showTab('order')">Pemesanan</button>
            <button class="tab-btn" onclick="showTab('management')">Manajemen Menu</button>
        </div>
        
        <!-- Order Tab -->
        <div id="order-tab" class="tab-content active">
            <div class="card">
                <h2>📋 Daftar Menu</h2>
                <div id="menu-list" class="menu-grid"></div>
            </div>
            
            <div class="card">
                <h2>🛒 Pesanan Anda</h2>
                <div id="order-items"></div>
                <div id="order-summary" class="order-summary" style="display:none;">
                    <h3>Ringkasan Pembayaran</h3>
                    <div id="summary-content"></div>
                </div>
                <div style="margin-top: 20px; display: flex; gap: 10px;">
                    <button class="btn btn-primary" onclick="calculateOrder()">Hitung Total</button>
                    <button class="btn btn-danger" onclick="clearOrder()">Hapus Semua</button>
                </div>
            </div>
        </div>
        
        <!-- Management Tab -->
        <div id="management-tab" class="tab-content">
            <div class="card">
                <h2>➕ Tambah Menu Baru</h2>
                <div class="form-group">
                    <label>Nama Menu</label>
                    <input type="text" id="new-menu-name" placeholder="Contoh: Soto Ayam">
                </div>
                <div class="form-group">
                    <label>Harga (Rp)</label>
                    <input type="number" id="new-menu-price" placeholder="Contoh: 25000">
                </div>
                <div class="form-group">
                    <label>Kategori</label>
                    <select id="new-menu-category">
                        <option value="Makanan">Makanan</option>
                        <option value="Minuman">Minuman</option>
                    </select>
                </div>
                <button class="btn btn-success" onclick="addMenu()">Tambah Menu</button>
            </div>
            
            <div class="card">
                <h2>📝 Kelola Menu</h2>
                <div id="management-list"></div>
            </div>
        </div>
    </div>
    
    <!-- Edit Price Modal -->
    <div id="edit-modal" class="modal">
        <div class="modal-content">
            <h3>Ubah Harga Menu</h3>
            <div class="form-group">
                <label>Harga Baru (Rp)</label>
                <input type="number" id="edit-price">
            </div>
            <div style="display: flex; gap: 10px;">
                <button class="btn btn-primary" onclick="savePrice()">Simpan</button>
                <button class="btn btn-danger" onclick="closeModal()">Batal</button>
            </div>
        </div>
    </div>
    
    <!-- Receipt Modal -->
    <div id="receipt-modal" class="modal">
        <div class="modal-content" style="max-width: 400px;">
            <div id="receipt-content" class="receipt"></div>
            <button class="btn btn-primary" onclick="closeReceiptModal()" style="width: 100%; margin-top: 20px;">Tutup</button>
        </div>
    </div>

    <script>
        let menuData = [];
        let orderItems = {};
        let editingIndex = -1;
        
        // Load menu data
        async function loadMenu() {
            const response = await fetch('/api/menu');
            menuData = await response.json();
            renderMenu();
            renderManagement();
        }
        
        function formatRupiah(num) {
            return 'Rp ' + num.toLocaleString('id-ID');
        }
        
        function renderMenu() {
            const container = document.getElementById('menu-list');
            container.innerHTML = '';
            
            // Group by category
            const makanan = menuData.filter(m => m.kategori === 'Makanan');
            const minuman = menuData.filter(m => m.kategori === 'Minuman');
            
            container.innerHTML = '<h3 style="grid-column: 1/-1; color: #667eea;">🍚 Makanan</h3>';
            makanan.forEach((menu, i) => {
                const index = menuData.indexOf(menu);
                container.innerHTML += createMenuCard(menu, index);
            });
            
            container.innerHTML += '<h3 style="grid-column: 1/-1; color: #667eea; margin-top: 20px;">🥤 Minuman</h3>';
            minuman.forEach((menu, i) => {
                const index = menuData.indexOf(menu);
                container.innerHTML += createMenuCard(menu, index);
            });
        }
        
        function createMenuCard(menu, index) {
            const qty = orderItems[menu.nama] || 0;
            return `
                <div class="menu-item ${qty > 0 ? 'selected' : ''}" id="menu-${index}">
                    <h4>${menu.nama}</h4>
                    <div class="price">${formatRupiah(menu.harga)}</div>
                    <span class="category">${menu.kategori}</span>
                    <div class="quantity-control">
                        <button onclick="updateQty('${menu.nama}', -1)">-</button>
                        <span>${qty}</span>
                        <button onclick="updateQty('${menu.nama}', 1)">+</button>
                    </div>
                </div>
            `;
        }
        
        function updateQty(nama, delta) {
            if (!orderItems[nama]) orderItems[nama] = 0;
            orderItems[nama] = Math.max(0, orderItems[nama] + delta);
            if (orderItems[nama] === 0) delete orderItems[nama];
            renderMenu();
            renderOrderItems();
        }
        
        function renderOrderItems() {
            const container = document.getElementById('order-items');
            if (Object.keys(orderItems).length === 0) {
                container.innerHTML = '<p style="color: #666; text-align: center; padding: 20px;">Belum ada pesanan. Pilih menu di atas.</p>';
                document.getElementById('order-summary').style.display = 'none';
                return;
            }
            
            let html = '<div style="margin-bottom: 20px;">';
            for (const [nama, qty] of Object.entries(orderItems)) {
                const menu = menuData.find(m => m.nama === nama);
                if (menu) {
                    html += `
                        <div class="order-line">
                            <span>${nama} x ${qty}</span>
                            <span>${formatRupiah(menu.harga * qty)}</span>
                        </div>
                    `;
                }
            }
            html += '</div>';
            container.innerHTML = html;
        }
        
        async function calculateOrder() {
            if (Object.keys(orderItems).length === 0) {
                alert('Silakan pilih menu terlebih dahulu!');
                return;
            }
            
            const items = Object.entries(orderItems).map(([nama, jumlah]) => ({ nama, jumlah }));
            
            const response = await fetch('/api/order/calculate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ items })
            });
            
            const result = await response.json();
            showReceipt(result);
        }
        
        function showReceipt(result) {
            let html = `
                <div class="receipt-header">
                    <h3>🍽️ RESTORAN KAMI</h3>
                    <p>Struk Pembayaran</p>
                    <p>${new Date().toLocaleString('id-ID')}</p>
                </div>
                <div class="receipt-items">
            `;
            
            result.items.forEach(item => {
                html += `
                    <div class="receipt-item">
                        <span>${item.nama} x${item.jumlah}</span>
                        <span>${formatRupiah(item.total)}</span>
                    </div>
                `;
            });
            
            html += `
                </div>
                <div style="border-top: 1px dashed #333; margin: 10px 0; padding-top: 10px;">
                    <div class="receipt-item">
                        <span>Subtotal</span>
                        <span>${formatRupiah(result.subtotal)}</span>
                    </div>
            `;
            
            if (result.diskonB1G1 > 0) {
                html += `
                    <div class="receipt-item" style="color: green;">
                        <span>Diskon B1G1</span>
                        <span>-${formatRupiah(result.diskonB1G1)}</span>
                    </div>
                `;
            }
            
            html += `
                    <div class="receipt-item">
                        <span>Pajak (10%)</span>
                        <span>${formatRupiah(result.pajak)}</span>
                    </div>
                    <div class="receipt-item">
                        <span>Biaya Pelayanan</span>
                        <span>${formatRupiah(result.biayaPelayanan)}</span>
                    </div>
            `;
            
            if (result.diskon10Persen > 0) {
                html += `
                    <div class="receipt-item" style="color: green;">
                        <span>Diskon 10%</span>
                        <span>-${formatRupiah(result.diskon10Persen)}</span>
                    </div>
                `;
            }
            
            html += `
                </div>
                <div class="receipt-total">
                    <div class="receipt-item">
                        <span>TOTAL</span>
                        <span>${formatRupiah(result.totalAkhir)}</span>
                    </div>
                </div>
                <div style="text-align: center; margin-top: 15px; border-top: 1px dashed #333; padding-top: 10px;">
                    <p>Terima kasih!</p>
                    <p>Selamat menikmati 😊</p>
                </div>
            `;
            
            document.getElementById('receipt-content').innerHTML = html;
            document.getElementById('receipt-modal').classList.add('active');
        }
        
        function closeReceiptModal() {
            document.getElementById('receipt-modal').classList.remove('active');
        }
        
        function clearOrder() {
            orderItems = {};
            renderMenu();
            renderOrderItems();
        }
        
        function renderManagement() {
            const container = document.getElementById('management-list');
            container.innerHTML = '';
            
            menuData.forEach((menu, index) => {
                container.innerHTML += `
                    <div class="management-item">
                        <div class="info">
                            <h4>${menu.nama}</h4>
                            <div class="details">${formatRupiah(menu.harga)} - ${menu.kategori}</div>
                        </div>
                        <div class="actions">
                            <button class="btn btn-primary" onclick="editPrice(${index})">Ubah Harga</button>
                            <button class="btn btn-danger" onclick="deleteMenu(${index})">Hapus</button>
                        </div>
                    </div>
                `;
            });
        }
        
        async function addMenu() {
            const nama = document.getElementById('new-menu-name').value.trim();
            const harga = parseInt(document.getElementById('new-menu-price').value);
            const kategori = document.getElementById('new-menu-category').value;
            
            if (!nama || !harga || harga <= 0) {
                alert('Silakan isi semua field dengan benar!');
                return;
            }
            
            await fetch('/api/menu', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nama, harga, kategori })
            });
            
            document.getElementById('new-menu-name').value = '';
            document.getElementById('new-menu-price').value = '';
            
            loadMenu();
        }
        
        function editPrice(index) {
            editingIndex = index;
            document.getElementById('edit-price').value = menuData[index].harga;
            document.getElementById('edit-modal').classList.add('active');
        }
        
        async function savePrice() {
            const newPrice = parseInt(document.getElementById('edit-price').value);
            if (!newPrice || newPrice <= 0) {
                alert('Masukkan harga yang valid!');
                return;
            }
            
            await fetch(`/api/menu/${editingIndex}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ harga: newPrice })
            });
            
            closeModal();
            loadMenu();
        }
        
        function closeModal() {
            document.getElementById('edit-modal').classList.remove('active');
            editingIndex = -1;
        }
        
        async function deleteMenu(index) {
            if (!confirm('Apakah Anda yakin ingin menghapus menu ini?')) return;
            
            await fetch(`/api/menu/${index}`, { method: 'DELETE' });
            loadMenu();
        }
        
        function showTab(tab) {
            document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
            document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));
            
            document.getElementById(tab + '-tab').classList.add('active');
            event.target.classList.add('active');
        }
        
        // Initialize
        loadMenu();
        renderOrderItems();
    </script>
</body>
</html>
        """;
    }
}
